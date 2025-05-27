
# Gerenciador de Senhas Seguras

Este projeto consiste em uma aplicação Java desenvolvida para permitir que usuários armazenem, gerenciem e protejam suas senhas de forma segura e intuitiva. O sistema visa resolver problemas comuns de segurança digital, como reutilização de senhas, armazenamento inseguro em texto simples e exposição a vazamentos de dados.

## Tipos de Usuário (Roles)

O gerenciador de senhas possui 2 tipos de usuários com permissões diferentes:

| Role    | ID | Descrição                        |
|---------|----| -------------------------------- |
| Admin   | 1L | Gerencia usuários e permissões   |
| Usuário | 2L | Acessa e gerencia suas senhas    |

---

## Funcionalidades

- Cadastro e autenticação de usuários
- Controle de acesso por papéis (roles)
- Geração de senha seguras que são enviadas por email
- APIs REST seguras com Spring Security

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.0
- Spring Security
- JWT (JSON Web Token)
- JPA/Hibernate
- MySQL
- Docker
- Java Mail Sender

---


## Como rodar o projeto

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seuusuario/gerenciador_senhas_seguras.git
   cd gerenciador_senhas_seguras
   ```
   
---

2. **Configure o banco de dados**
    - Edite o arquivo `src/main/resources/application.properties` com as credenciais do seu banco MySQL.

---

3. **Build e execute**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
---

