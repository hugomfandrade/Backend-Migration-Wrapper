# Backend (Spring Boot)

## Migration Wrapper Application

Useful for iterative migration of a legacy service to this one. 

By default, calls another service and returns its response. 

## CI/CD Integration

There ware two types of CI/CD integration to the Azure App service

### Azure DevOps

With azure-pipeline.yml, and an Azure DevOps account, it will run the 
"mvn clean install" commands and deploy the jar to the App Service 
configured in Azure DevOps portal.

### .jar in "release" dir

With the appropriate configuration in the Deployment Center of the 
App Service in the Azure Portal, it will add a hook to this repo and 
fetch this repo. On startup, it will find the .jar file that is in the 
"release" directory. 

Add the attribute 'outputDirectory' with value 'release' to the
'configuration' attribute of the 'spring-boot-maven-plugin' plugin and
run 'mvn clean install' (or run the 'repackage' lifecycle for maven).