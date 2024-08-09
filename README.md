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
WHERE filmId IN (SELECT filmId
                  FROM likes
                  GROUP BY filmId
                  ORDER BY COUNT(userId) desc
                   LIMIT 10);
```
Ex.2
Output id and login of friends on id user = 1:

```sql
SELECT u.login,
       u.userId
FROM user AS u
WHERE u.userId IN (SELECT uf.friend_id
                    FROM users_friends AS uf
                    WHERE uf.userId = 1);
```
