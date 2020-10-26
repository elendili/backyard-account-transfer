### TODO
- Add transfer accounts
- Check concurrent access
- Work with external DB
- Web-interface

### Issues
- I was unable to make work Quarkus tests with Log4j2
- can't do `EntityManager.getTransaction()`, because exception: `Not supported for JTA entity managers`
- `em.persist(from);` doesn't work on update, was forced to use `UPDATE`

### Memo
- quarkus-arc is context-dependency injection on build time.
Wiring on build time faster than wiring by Wield
(https://quarkus.io/blog/quarkus-dependency-injection/)  
