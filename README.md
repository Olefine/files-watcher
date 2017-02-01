# Description

## Main purpose

This repository introduces naive way to implement some kind of *spark*. It allows you run application server(implemented using scalatra) and run jobs for files within current directory. 

It works with two kind of workers:

1. *Standalone* - worker deploys into the same machine where running main application and uses full available resources, if file you want to process has size more then available RAM you will get an error.
2. *Remote* - worker deploys into the amazon's ec2. When deploy started we calculating file size you want to process and choose appropriate instance.Type. To do it works you need change config and setup env variables for amazon(**AWS_SECRET_ACCESS_KEY**, **AWS_ACCESS_KEY_ID**) and change .pem key location in config.

## Running jobs

Now when you clicking on fileName from webUi it automaticly runs wordsCount job. For now, if you want to run your own job, you need write expressions and place it into the file. Your expression must have type 

```Function2[String, String]```

In future I will add more user friendly ways to create job like ACE editor and you as a user will have ability not to specify returning type of your job.

**NOTE**: This repository's purpose just get familiar with scala programming language and Actor model. Please, feel free to contribute if you want, but it will never run in production or release into maven central.

## Roadmap

-----------------
- [ ] ACE Editor to allow create code for job in fly
- [ ] Streaming support
- [ ] AMQP support
- [ ] Package workers to Docker

## Build & Run ##

```sh
$ cd files-watcher
$ ./sbt
> ~ ;build ;server
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
