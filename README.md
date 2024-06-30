# java-filmorate
Template repository for Filmorate project.

## ER-diagram

![filmorate](ER-diagram.png)

Example Sql query:
Ex.1 
Top 10 film name on count likes:

```sql
SELECT name
FROM film
WHERE film_id IN (SELECT film_id
                  FROM likes
                  GROUP BY film_id
                  ORDER BY COUNT(user_id) desc
                   LIMIT 10);
```
Ex.2
Output id and login of friends on id user = 1:

```sql
SELECT u.login,
       u.user_id
FROM user AS u
WHERE u.user_id IN (SELECT uf.friend_id
                    FROM users_friends AS uf
                    WHERE uf.user_id = 1);
```
