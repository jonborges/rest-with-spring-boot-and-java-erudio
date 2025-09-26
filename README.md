# Guia do Projeto Spring Boot, sendo organizado por sessões de aulas de determinado assunto.

---

## 4. Primeiros Passos no Spring Boot

Nesta seção, configuramos o projeto Spring Boot e criamos o primeiro endpoint.  

- Utilizamos o [Spring Initializr](https://start.spring.io/) para gerar o esqueleto do projeto.  
- Criamos o `HelloController` que responde com a mensagem **"Hello, World!"** no endpoint `/greetings`.  
- É possível enviar o parâmetro `?name=` para alterar o nome exibido na resposta.  

Esse passo garante que a aplicação está funcionando corretamente antes de avançarmos para funcionalidades mais complexas.

---

## 5. Primeiro Contato com Parâmetros `@`

- Apresentação das anotações `@RestController` e `@RequestMapping`.  
- Exemplos práticos com operações matemáticas simples para demonstrar o uso de parâmetros.

---

## 6. Como Usar Alguns Verbos HTTP

- Introdução aos métodos HTTP: `POST`, `PUT` e `DELETE`.  
- Exemplos de métodos simples, como `findAll`, para manipulação de dados.

---

## 7. Integrando com MySQL

- Instalação do MySQL.  
- Integração do Spring Boot com MySQL utilizando a ferramenta HeidiSQL para gerenciamento do banco.

---

## 8. Trabalhando com Logs

- Personalização dos logs da aplicação.  
- Configuração de logs globais para melhor monitoramento.

---

## 9. Conhecendo o Padrão de Projeto DTO

- Introdução ao padrão **DTO (Data Transfer Object)** para melhor organização e segurança dos dados.

---

## 10. Versionamento de API

- Aula teórica sobre versionamento de APIs.  
- Será utilizado o **FlyWay** para gerenciar versões do banco de dados.
  
---

## 11. Suporte ao Myflay

- Utilização do Myflay para criação do banco, organização dos arquivos no Resources/migration.  
- Foi mostrado como utilizar o Prompt para rodar as mudanças, além do Plugin Maven + Sql.


---

## 12. Configuração do Serialization

- Personalizar e controlar o Json, alterando dados, ocultando dados importantes.  
- Como lidar com formatos de campos Data.
- Propriedades condicionais

- ---

## 13. Lidando com Vários tipos de Arquivos

- Adicionado ao Pom jackson-dataformat-xml e yaml 
- Pasta config e controller configurados com todos os retornos de tipos de arquivos
- Por fim, nova pasta serialization converter com o YAMLMapper configurado
- ATENÇÃO: Foi necessário adicionar a versão 2.15.2 do jackson-dataformat-yaml de forma MANUAL, pois versões novas estão com conflito com o método YAMLMapper

- ---
## 14. Configurando Hateoas e Novos testes Mockito

- Configurado o Hateoas com os Links de todas as operações do CRUD  
- Testes para todas as operações do Service
- Simulação de exceções

- ---
## 15. Adicionando Documentação com Swagger 

- Configurado Swagger com as devidas @ApiResponse e @Content e tipos de retorno
- Criada pasta *Docs com interfaces com a documentação para organizar e limpar Controllers
- Novo db_migration CREATE-TABLE-BOOK
- Adicionada entidade Book, junto de seu Controller, Service e Repositório

- - ---
## 16. Configuração do Docker Container (meu Docker Hub: https://hub.docker.com/repositories/jonborges)

- Novo arquivo para exemplificar containers com Docker
- Criação e envio da imagem no Docker Hub
- Teórica: O que são containers, images, BIN/EXE
- Acessando a image que está no Docker Hub diretamente pelo terminal
- Conhecendo os comantos mais utilizados do Docker [Comandos Docker mais utilizados (PDF)](https://www.dropbox.com/scl/fi/sacdhpkozhy42d6s4uq5r/comandos_docker_atualizados.pdf?rlkey=3fefv7sgtj9bm2ykvrovsbvlh&st=q0pys1l6&raw=1)
- Conhecendo Arquitetura do Docker, Gerenciamento de Docker Images e Comandos stats, system 

- - ---
## 17. Configuração dos Testes de Integração

- TestContainers e Rest-assured adicionados ao Pom
- Construção do Yaml dos Testes para acessar o banco de forma dinâmica
- Novos testes e nova organização de pastas
- Importando Swagger Api-Doc para utilizar no Postman

- - - ---
## 18. Configuração Cros-Origin Resource Sharing (Cors)

- Habilitando Cors de forma local em cada operação, depois alterando para Global no WebConfig
- Criação de testes para validar se o CORS está funcionando 
- Testando diferentes Origin e cenários no Postman

- - - - ---
## 19. Verbo PATCH + Novos testes em Json, Xml e Yaml

- Criando o Disable Person para exemplificar o Patch, um serviço que desabilita uma Person pelo Id
- Criação de testes para a nova rota Patch em Json
- Terminado arquivo Json, agora são criados testes de TODAS as operações em Json, Yaml e Xml

- - - - ---
## 20. Query Params, Hal, Parâmetros de Paginação, Multiformatos testes, testes de repository

- Busca paginada, Mookaru e validações com testes, adicionado Pageable nos endpoints 
- Ordenação com Direction, Aplicação do Hal junto com Hateoas para proporcionar mais informações
- Novo método: Buscar pessoa/livro pelo NOME
- Novas pastas de Testes, nova separação por Json, Xml e Yaml
- Novos testes para os Repository

- - - - ---
## 21. Upload e Download de arquivos

- Introdução ao conceito de upload e donwload em Spring Boot
- Adicionada a configuração no aplication.yml
- Criado Serviço e Controller responsáveis pelo upload e download
- Nova pasta no Postman para testar






