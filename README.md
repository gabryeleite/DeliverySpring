# Restaurante e Pedidos - Sistema de Gestão e Consumo
Este projeto é uma aplicação web desenvolvida em Java utilizando Spring Web, JPA, e Thymeleaf. Ele permite que restaurantes sejam cadastrados e geridos por administradores, enquanto consumidores podem visualizar cardápios e montar pedidos.

## Funcionalidades

### Aplicação de Administração
- **Cadastro de Restaurantes:** Permite que o administrador cadastre, edite, liste e apague restaurantes. </br>
- **Gestão de Cardápios:** Administradores podem adicionar, editar, listar e excluir itens de cardápio de cada restaurante. </br>
- **Persistência de Dados:** Todos os dados de restaurantes e itens de cardápio são armazenados em um banco de dados relacional, gerido pelo JPA. </br>

### Aplicação de Consumidor 
- **Listagem de Restaurantes:** Consumidores podem visualizar uma lista de todos os restaurantes cadastrados. </br>
- **Consulta de Cardápios:** Permite que o consumidor acesse os cardápios dos restaurantes disponíveis. </br> 
- **Montagem de Pedidos:** Consumidores podem escolher itens dos cardápios e montar um pedido, que é gravado na sessão Web do usuário e persistido no banco de dados. </br>

## Tecnologias Utilizadas
- Java 11+
- Spring Boot
- Spring Web
- Spring Data JPA
- Thymeleaf
- MySQL
