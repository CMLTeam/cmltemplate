# Docker strategy
               
## Where to store data

Available options

- In container (default)
  - Cons:
    - not reliable: data loss on container re-creation
    - hard to use: not easy to read/write data
- In docker volume
  - Cons:
    - hard to use: not easy to read/write data
- In mounted folder
  - Cons:
    - pollutes work folder
    - issues with permissions in mounted folder

Based on considerations above we chose to use named local volumes.

This is how it looks:

```
$ docker volume ls
DRIVER    VOLUME NAME
local     cmltemplate-mongo_mongo_configdb
local     cmltemplate-mongo_mongo_data
local     cmltemplate-rabbit_rabbit_data
```

(In real usage it will be `real_project_name` instead of `cmltemplate`).

If you want to reach physical location of a volume's data just do

```
cd "$(docker volume inspect cmltemplate-mongo_mongo_data \
    | jq -r '.[0].Mountpoint')"
```

You might need `sudo` though to access the files.
   
## Services configuration

We considered an approach to map the config files of a service to the host folder for ease of change. But this causes more complexity than we want:
  - Aforementioned permissions issues
  - Unneeded flexibility that can be unsustainable

So instead it's better to try to configure the services via it's .yml file:
  - environment vars
  - command (i.e. see how we've set up the slow log for [mysql](mysql.yml))

## One .yml file vs many

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
