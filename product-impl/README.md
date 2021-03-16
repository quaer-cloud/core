### Postgres configuration

This configuration is only necessary if you are not using the instance provided by `docker-compose.yml`.

First, you need to create the database, the user, and the password yourself. The application expects it to be running on `localhost` on the default port (`5432`), and it expects there to be a database called `shopping_cart`, with a user called `shopping_cart` with password `shopping_cart` that has full access to it. This can be created using the following SQL:

```sql
CREATE DATABASE product;
CREATE USER product WITH PASSWORD 'product';
GRANT ALL PRIVILEGES ON DATABASE product TO product;
```
