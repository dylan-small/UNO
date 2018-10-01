Hi! My source files can be found under src>com>dylan>game and src>com>dylan>main (I like to use packages to manage my code)

You can run my code by changing into the current directory, then running:

java -jar UNO.jar

If you'd like to see my other projects, you can take a quick look at the link below
https://github.com/dylan-small

For my UNO game, I used a Stack for the deck, which makes sense because a deck of cards is a last in first out structure.
I took cards from the top of the deck. Although I could have used a Queue and acheived the same efficiency, the Stack just made more sense to me.

I took somewhat of an OOP approach to this game. Although the only Objects I could create would be a Player class and 
Card class, the rest of the programming was pretty procedural. On this specific game, efficiency was important to keep in mind as well. I used many abstractions
to make my code not only easier to read, but easier to program as well. I also tried to use the least amount of static methods and variables as they tend to 
conflict with OOP design. I also chose Java as my language for this since it's easy to run on almost all Operating Systems.

I also made sure to comment out my methods and functions to accelerate its readability. I worked for ~3.5 hours on this project, and to be honest
it was a really good excersize!