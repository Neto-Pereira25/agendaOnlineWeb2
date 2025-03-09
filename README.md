# 📅 Sistema de Agenda Online

Este é um sistema de Agenda Online desenvolvido em Spring Web MVC, utilizando Thymeleaf como motor de templates e MySQL como banco de dados. O sistema permite que os usuários se cadastrem, façam login e gerenciem seus contatos de forma segura e eficiente.

- 📌 Funcionalidades

- 📌 Cadastro de usuários

- 🔑 Autenticação de usuários (Login/Logout)

- 🔄 Atualização de perfil e senha

- 📇 Cadastro, listagem, edição e remoção de contatos

- 🔎 Pesquisa de contatos por nome

- ☎ Cada contato pode ter um ou mais números de telefone

## 🚀 Tecnologias Utilizadas

- Java 21

- Spring Web MVC

- Thymeleaf

- JDBC (sem JPA)

- MySQL

- Bootstrap (para estilização da interface)

## 🛠 Requisitos para rodar o projeto

- Java 21 ou superior

- MySQL 8.x

- Maven

- Servidor Tomcat 9+ (se necessário)

## 📦 Configuração do Banco de Dados

- Crie um banco de dados no MySQL:
```
CREATE DATABASE AgendaOnline;

```


- Execute o script SQL localizado em src/main/resources/db/schema.sql para criar as tabelas.

- Configure as credenciais do banco criando um o arquivo db.properties na raiz do projeto e insira o código abaixo com as devidas modificações:
```
db-url=jdbc:mysql://localhost:3306/AgendaOnline
user=seu_usuario
password=sua_senha
useSSL=false
```

## ▶ Como Rodar o Projeto

- Clone o repositório:

```
git clone git@github.com:Neto-Pereira25/agendaOnlineWeb2.git
cd agenda-online
```
ou


```
git clone https://github.com/Neto-Pereira25/agendaOnlineWeb2.git
cd agenda-online
```

- Compile e execute o projeto com Maven:

```
mvn spring-boot:run
```

- Acesse no navegador: http://localhost:8080

## 🔖 Endpoints Principais

- / → Página de cadastro de usuário

- / → Página de login
  
- /profile → Mostra os dados do usuário logado

- /updateProfile → Edita dados do usuário

- /contact/listAllContact → Lista os contatos do usuário logado

- /contact/updateContact/{id} → Edita um contato

- /contact/removeContact/{id} → Remove um contato

- /contact/findContactByName → Pesquisa contatos pelo nome

## 📜 Licença

Este projeto é de código aberto e pode ser utilizado livremente para fins educacionais e profissionais.

💡 Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests. 😃
