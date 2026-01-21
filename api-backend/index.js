require("dotenv").config({path: __dirname + "/.env"} ); // Injeção absoluta do .env
const express = require("express");
const { Pool } =  require("pg");
const process = require("process");
const cors = require("cors");

const app = express();
const port = process.env.PORT || 3000;

const DATABASE_URL = process.env.DATABASE_URL;
const SCHEMA = process.env.DB_SCHEMA ? process.env.DB_SCHEMA.trim() : undefined;

// Verificação & Checagem de Variáveis
if (!SCHEMA) {
    console.error("ERRO: variável DB_SCHEMA não definida ou está vazia. Verifique seu arquivo .env.");
    process.exit(1);
};

if (!DATABASE_URL) {
    console.error("ERRO: variável DATABASE_URL não definida. Crie um .env na raiz ou defina a variável.");
    process.exit(1);
};

// Config para JSON
app.use(express.json());
app.use(cors());

// Conexão com Banco de Dados
const pool = new Pool({
    connectionString: DATABASE_URL,
    max: 20,
    idleTimeoutMillis: 30000,
    connectionTimeoutMillis: 2000,
});

// Rota Teste
app.get("/", (req, res) => {
    res.send("API Vence Quando - Online")
});

// Rota Teste do Banco de Dados
app.get("/test-db", async (req, res) => {
    try {
        const result = await pool.query(`SELECT * FROM ${SCHEMA}.users`)
        res.json({
            message: "Conexão com banco de dados realizada!",
            total_usuarios: result.rowCount,
            usuarios: result.rows
        });
    } catch (err) {
        console.log(err)
        res.status(500).json({ error: "Erro ao conectar no banco", details: err.message });
    }
})

// ROTA DE LOGIN
app.post("/login", async (req, res) => {
    const { email, password } = req.body;

    if (!email || !password) return res.status(400).json({ error: "Dados incompletos!" })

    try {
        const query = `SELECT id, name, email FROM ${SCHEMA}.users WHERE email = $1 AND password = $2`;
        const result = await pool.query(query, [email, password]);

        if (result.rows.length === 0) return res.status(401).json({ error: "Credenciais Inválidas!" })

        res.status(200).json({ message: "Login Realizado!", user: result.rows[0] });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Erro interno do servidor." })
    }
});

// ROTA DE PRODUTOS (CRUD)
app.get("/products", async (req, res) => {
    try {
        const query = `
        SELECT id, name, category, expiration_date FROM ${SCHEMA}.products
        ORDER BY expiration_date ASC
        `;

        const result = await pool.query(query);

        const productsWithStatus = result.rows.map(product => {
            const hoje = new Date();

            hoje.setHours(0, 0, 0, 0);
            const dataVencimento = new Date(product.expiration_date);
            dataVencimento.setHours(0, 0, 0, 0);

            // Calcula a diferença em millisegundos e converte pra dias
            const diffTime = dataVencimento - hoje;
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

            let status = "OK";
            if (diffDays <= 0) status = "VENCIDO";
            if (diffDays <= 3) status = "URGENTE";
            if (diffDays <= 7) status = "ATENCAO";
            
            const dataFormatada = dataVencimento.toLocaleDateString("pt-BR");

            return {
                id: product.id,
                name: product.name,
                category: product.category,
                expiration_date: dataFormatada,
                status: status,
                daysRemaining: diffDays
            };
        });
        
        res.status(200).json({products: productsWithStatus});

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Erro ao buscar produtos." })
    }
})

app.post("/products", async (req, res) => {
    try {
        const { name, category, expiration_date, user_id } = req.body;

        if (!name || !category || !expiration_date || !user_id) {
            return res.status(400).json({ error: "Dados incompletos!" });
        }
        // Front end envia data no formato dd/mm/yyyy (lembrar de converter)
        const query = `
        INSERT INTO ${SCHEMA}.products (name, category, expiration_date, user_id)
        VALUES ($1, $2, $3, $4)
        RETURNING *
        `;

        const result = await pool.query(query, [name, category, expiration_date, user_id]);

        res.status(201).json({ message: "Produto criado com sucesso!", product: result.rows[0] });  
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Erro ao criar produto." })
    }
})

app.put("/products/:id", async (req, res) => {
    try {
        const id = req.params.id;
        const { name, category, expirationDate } = req.body;

        const query = `
            UPDATE ${SCHEMA}.products 
            SET name = $1, category = $2, expiration_date = $3
            WHERE id = $4
            RETURNING *
        `;

        const values = [name, category, expirationDate, id];
        const result = await pool.query(query, values);

        if (result.rowCount === 0) {
            return res.status(404).json({ error: "Produto não encontrado" });
        }

        res.status(200).json(result.rows[0]);

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Erro ao atualizar produto" });
    }
});

app.delete("/products/:id", async (req, res) => {
    try {
        const id = req.params.id;

        const query = `DELETE FROM ${SCHEMA}.products WHERE id = $1 RETURNING id`;
        const result = await pool.query(query, [id]);

        if (result.rowCount === 0) {
            return res.status(404).json({ error: "Produto não encontrado" });
        }

        res.status(200).json({ message: "Produto deletado com sucesso" });

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Erro ao deletar produto" });
    }
});

app.listen(port, () => {
    console.log(`Servidor rodando em http://localhost:${port}`);
})
