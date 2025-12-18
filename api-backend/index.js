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

app.listen(port, () => {
    console.log(`Servidor rodando em http://localhost:${port}`);
})
