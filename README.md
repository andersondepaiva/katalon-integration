# katalon-integration
Projeto de integração com os testes feitos no Katalon Studio

# Arquitetura
![alt text](https://i.ibb.co/S7Nkvjv/Captura-de-tela-de-2019-01-26-10-43-22.png)

# Componentes
* Auth
    * API autenticação JWT (Spring Boot e Security)
* Core
    * Biblioteca Jar com classes base (Business, Repository, Client...) para todos os projetos.
* Discovery
    * Eureka Server (Spring Boot)
* Gateway
    * API Gateway Zuul utilizando Spring Boot, também é responsável pela validação do JWT.
* Katalon-Executor
    * Aplicação Spring Boot responsável pelas execuções dos scripts Katalon, todas as execuções são consumidas das filas do RabbitMQ
* Katalon-Integration
    * API Spring Boot responsável por processar as requisições do client em VueJS:
        * CRUD Projetos (Inclusive integração com repositórios GIT)
        * CRUD das Configurações da localização do executável do Katalon
        * Recebe solicitações de execuções de Test Suítes do Katalon, publicando uma mensagem no RabbitMQ que será consumido pelo Katalon-Executor.
* Katalon-Portal
    * Front-end implementado em VueJS utilizando os frameworks Vuetify e Vuex.
* Pessoa
    * API Spring Boot responsável pelo CRUD de usuários do sistema.
