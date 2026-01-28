// api-backend/migrate.js
const { Pool } = require('pg');
require('dotenv').config();

// Configuração da conexão
const pool = new Pool({
    user: process.env.DB_USER,
    host: process.env.DB_HOST,
    database: process.env.DB_NAME,
    password: process.env.DB_PASSWORD,
    port: process.env.DB_PORT,
});

const runMigrations = async () => {
    try {
        console.log('Iniciando criação do banco...');

        await pool.query(`
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                name VARCHAR(100)
            );
        `);
        console.log('Tabela "users" criada.');

        await pool.query(`
            CREATE TABLE IF NOT EXISTS products (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                category VARCHAR(50),
                expiration_date DATE NOT NULL,
                user_id INT REFERENCES users(id) ON DELETE CASCADE
            );
        `);
        console.log('Tabela "products" criada.');

        const userCheck = await pool.query('SELECT count(*) FROM users');
        
        if (parseInt(userCheck.rows[0].count) === 0) {
            console.log('Populando banco com dados de teste...');

            await pool.query(`
                INSERT INTO users (email, password, name) VALUES 
                ('aluno@teste.com', '123456', 'Aluno Teste'),
                ('professor@teste.com', 'admin', 'Professor Avaliador');
            `);

            await pool.query(`
                INSERT INTO products (name, category, expiration_date, user_id) VALUES 
                ('Leite Integral', 'Alimento', CURRENT_DATE + 2, 1),
                ('Dipirona', 'Remédio', CURRENT_DATE + 5, 1),
                ('Arroz 5kg', 'Alimento', CURRENT_DATE + 30, 1);
            `);
            
            console.log('Dados de teste inseridos com sucesso!');
        } else {
            console.log('O banco já contém dados. Pulei a etapa de Seed.');
        }

        console.log('Migrations finalizadas!');

    } catch (err) {
        console.error('Erro na migration:', err);
    } finally {
        await pool.end();
    }
};

runMigrations();