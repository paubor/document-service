# Document Service

The document service is a DropWizard microservice,
which lets you upload files associated with categories,
search them and download them again.

## Getting Started

Here a list of steps needed to run and test the application

### Prerequisites

You need to have the following programs installed on your local machine:

- Java 11 or higher
- Maven

### Compilation

In order to compile the application run the following commands in your command line:

1. ```mvn clean package```
2. ```java -jar target/document-service.jar```

You should see now a usage line which should look similar to this:
~~~
usage: java -jar project.jar [-h] [-v] {server,check} ...

positional arguments:
  {server,check}         available commands

named arguments:
  -h, --help             show this help message and exit
  -v, --version          show the application version and exit
~~~

### Configuration

To change the default value of the location where the uploaded files will be stored, 
1. open `config.yml` with an editor
2. change the value of `filePath: uploadedFiles` to `filePath: <yourPath>`

### Running the service

Simply run ```java -jar target/document-service.jar server config.yml```

If everything worked, you should see a huge banner with **Document Service**, followed by a bunch of output, ending with 
~~~
org.eclipse.jetty.server.Server: Started
~~~


### Getting the documentation

The documentation for this project consists of built-in [Swagger](https://swagger.io/) endpoints for both the Web UI
and the actual `swagger.json` or `swagger.yaml` documentation.
In order to get the swagger UI, once the service is up and running, navigate to <http://localhost:8080/swagger>.
For the plain documentation file, navigate to <http://localhost:8080/swagger.json> or <http://localhost:8080/swagger.yaml>.

Now you can explore the documentation and try out the different endpoints.

Alternatively, you can use other tools like [Postman](https://www.postman.com/) or [curl](https://curl.haxx.se/) to test the endpoints.



## Deployment to a Production Environment

I would not recommend to deploy this project into a production environment yet,
since the files are stored at the server,
which is not the desired choice. Instances in cloud environments might be killed or recycled, which means a loss of data.

I was playing with the idea to use Firestore as a cloud solution for a while,
but dropped the attempt, because it would take time to document here how to set it up.
However, having played around with Firestore I think it would be a good persistent solution.

## Technical details

I decided to implement the service as a file-server, with 3 endpoints:
1. Upload a document with a category (using multipart form-data, has the ability to upload files)
2. Search for documents (by optionally passing a query parameter with category)
3. Download file with a given uid

I decided to treat the file differently from the other data.
Files are not stored with the filename which is given by the user, but rather by a uuid.
Additionally, meta-data is stored in a json file which basically maps a file-uuid to
1. The categories: a list (semantically a set) of strings (all lowercase)
2. The original file name provided by the user when uploading

This allows the service to download the file with the appropriate extension and it will be fully
usable by the user, once downloaded.
The uuid is used for intermediate steps and also to download.
Another cool feature of uuids, is that multiple file names will not be a problem.

Further work would be, to use dependency injection which I did not implement for now but should be an easy fix.
I rather spent some time on including swagger to have a full documentation but also an interactive gui to play with the servcice.

## Built With

* [DropWizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Author

* **Paul Borek** <borek.paul@gmail.com>
