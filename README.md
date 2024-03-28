# akka-stream-splitter

This project is an end to end example of how to split a stream of data into multiple streams using Akka Streams where
each substream performs a distinct action on a subset of messages.

The first implementation is the four chef example that is implemented on
[Akka Streams documentation](https://doc.akka.io/docs/akka/current/stream/stream-parallelism.html#combining-pipelining-and-parallel-processing).
The second is an example of how to split a stream of data into multiple streams using Akka Streams where each substream
performs a distinct action on a subset of messages. The third is an example of how to split a stream with that same
distinct
actions needed to be performed on a batch of messages where the messages in the batch need to be maintained.

## Running the project

To run the project, execute the following command:

```shell
docker compose up --build
```

Output will be:

```shell
server-1  | ***** Running the akka stream four chefs example *****
server-1  | Frying pan 1: ScoopOfBatter(0)
server-1  | Frying pan 1: ScoopOfBatter(1)
server-1  | Frying pan 1: ScoopOfBatter(2)
server-1  | Frying pan 1: ScoopOfBatter(3)
server-1  | Frying pan 2: HalfCookedPancake(0)
server-1  | Frying pan 2: HalfCookedPancake(1)
server-1  | Frying pan 2: HalfCookedPancake(2)
server-1  | Frying pan 2: HalfCookedPancake(3)
server-1  | Pancake(0)
server-1  | Pancake(1)
server-1  | Pancake(2)
server-1  | Pancake(3)
server-1  | [INFO] [03/28/2024 17:34:21.833] [ForkJoinPool.commonPool-worker-19] [CoordinatedShutdown(akka://four-chefs)] Running CoordinatedShutdown with reason [ActorSystemTerminateReason]
server-1  | Pancakes are done!
server-1  | ***** Finished running the akka stream four chefs example *****
server-1 exited with code 0
```