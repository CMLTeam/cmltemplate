# Docker strategy

## One file vs many

TODO, see https://stackoverflow.com/questions/30233105/docker-compose-up-for-only-certain-containers

## Recipes
             
### List

```shell
docker ps        # list running containers
docker ps -a     # list all containers
docker volume ls # list volumes
```

### Clear all

DANGER!!! USE WITH CARE!!! THIS **WILL** CAUSE DATA LOSS!!!

```shell
# rm all containers
docker rm -f $(docker ps -a -q)
 
# rm all volumes
docker volume rm $(docker volume ls -q)
```
