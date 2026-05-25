Below are steps for connecting mongodb from docker.

From powershell run these below command:
1. docker run --name my-mongo -p 27017:27017 -d mongo
2. Use code with caution.--name: Assigns a custom name to your container.-p 27017:27017: Maps the container's internal port to your host machine's port.-d: Runs the container in detached mode (in the background).mongo: Specifies the official MongoDB image. You can add a tag like mongo:latest or mongo:7.0 for specific versions.2. Recommended: Persistent Data & SecurityDocker containers are ephemeral; if you delete the container, your data is lost. To save your data and secure the instance with a root user, use this command:bash 
Comamnd : docker run -d  --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password123  -v mongodb_data:/data/db  mongo

Use code with caution.-e MONGO_INITDB_ROOT_USERNAME/PASSWORD: Sets up admin credentials.-v mongodb_data:/data/db: Creates a Docker volume to store your database files permanently on your host machine.

3. To run mongo 
 Option A: For modern MongoDB (v6.0+)Most recent images use mongosh.
   Run this command:
   docker exec -it <container_name_or_id> mongosh
   Option B: For older MongoDB versionsOlder versions use the legacy mongo
   command:
   docker exec -it <container_name_or_id> mongo