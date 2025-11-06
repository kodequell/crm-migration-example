# CRM Migration Example

This repository provides an example pattern for migrating data from one system into another. 
It is focused on reliable, repeatable data transfers and split into two Maven modules so 
migration logic can be reused independently from the runnable migration job.

The crm-migration-base is a Spring Boot autoconfiguration library that provides domain 
models, mapping and transformation utilities, connector interfaces for source and destination
systems. 

As an autoconfigure library it  registers sensible default beans and properties when placed 
on the classpath, so other Spring Boot applications can consume the migration functionality 
by simply adding the dependency (or enabling the auto-configuration) and overriding 
configuration properties or beans as needed.