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
server-1  | Frying pan 2: HalfCookedPancake(1)
server-1  | Pancake ready: Pancake(0)
server-1  | Frying pan 2: HalfCookedPancake(0)
server-1  | Frying pan 2: HalfCookedPancake(3)
server-1  | Frying pan 2: HalfCookedPancake(2)
server-1  | Pancake ready: Pancake(1)
server-1  | Pancake ready: Pancake(3)
server-1  | Pancake ready: Pancake(2)
server-1  | [INFO] [10/21/2024 22:45:52.153] [ForkJoinPool.commonPool-worker-19] [CoordinatedShutdown(akka://four-chefs)] Running CoordinatedShutdown with reason [ActorSystemTerminateReason]
server-1  | Pancakes are done!
server-1  | ***** Finished running the akka stream four chefs example *****
server-1  | 
server-1  | 
server-1  | ***** Running the akka stream splitter example *****
server-1  | Pancake ready: BlueberryPancake(0)
server-1  | Pancake ready: BananaPancake(1)
server-1  | Pancake ready: ChocolateChipPancake(2)
server-1  | Pancake ready: BlueberryPancake(3)
server-1  | Pancake ready: ChocolateChipPancake(5)
server-1  | Pancake ready: ChocolateChipPancake(8)
server-1  | Pancake ready: BananaPancake(4)
server-1  | Pancake ready: BananaPancake(7)
server-1  | Pancake ready: BlueberryPancake(6)
server-1  | Pancakes are done!
server-1  | ***** Finished running the akka stream splitter example *****
server-1  | 
server-1  | 
server-1  | [INFO] [10/21/2024 22:45:52.192] [ForkJoinPool.commonPool-worker-19] [CoordinatedShutdown(akka://splitter)] Running CoordinatedShutdown with reason [ActorSystemTerminateReason]
server-1  | ***** Running the akka stream batch splitter example *****
server-1  | Pancake batch ready: PancakeBatch [ticketId=1, pancakes=[BananaPancake(1)]]
server-1  | Pancake batch ready: PancakeBatch [ticketId=2, pancakes=[BlueberryPancake(2), BlueberryPancake(3)]]
server-1  | Pancake batch ready: PancakeBatch [ticketId=3, pancakes=[BlueberryPancake(4), ChocolateChipPancake(5), ChocolateChipPancake(6), ChocolateChipPancake(7)]]
server-1  | Pancake batch ready: PancakeBatch [ticketId=4, pancakes=[BlueberryPancake(8)]]
server-1  | Pancake batch ready: PancakeBatch [ticketId=5, pancakes=[BlueberryPancake(11), ChocolateChipPancake(9), ChocolateChipPancake(10)]]
server-1  | Pancakes are done!
server-1  | ***** Finished running the akka stream batch splitter example *****
server-1  | [INFO] [10/21/2024 22:45:52.222] [ForkJoinPool.commonPool-worker-19] [CoordinatedShutdown(akka://batch-splitter)] Running CoordinatedShutdown with reason [ActorSystemTerminateReason]
server-1 exited with code 0

```