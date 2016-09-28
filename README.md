# PathFinding Visualization

This program visualizes common map traversal algorithms while allowing the user to modify the map with varying weights

![](http://imgur.com/3gTc1F2.png)

## Videos
I have posted a few videos representing my program on YouTube:
* [Hierarchical Pathfinding](https://www.youtube.com/watch?v=k-HOcrFSQwI)
* [Dijkstra's Algorithm Pathfinding](https://www.youtube.com/watch?v=YmZVbLc1b04)
* [A* Pathfinding](https://www.youtube.com/watch?v=Wxqbyja6Anc)

**In order to compile this program, you will need to include the libraries in the /libs/ folder**

## Algorithms represented:
* A*
* Dijkstra's Algorithm
* Hierarchical Pathfinding

### A*
![](http://imgur.com/ssBVybs.png)

### Dijkstra's Algorithm
![](http://imgur.com/TCB87vB.png)

### Hierarchical Pathfinding
![](http://imgur.com/t93dWbt.png)

## Controls
* Left click - Colors in a block based on the current weight value
* Right click - Sets a block to a weight value of 0.0 (empty)
* Shift + left click - Creates or moves the 'start' block
* Shift + right click - Creates or moves the 'end' block
  * You can only run the simulation when a start and end block are selected. You can tell if it is possible to run the program because the "Run Simulation" button at the bottom will be enabled.
* Spacebar - Starts the simulation with the currently selected algorithm and graph
* Alt - Enables debug information, also shows 'Regions'
* Ctrl - Enables mouse based pathfinding with a 'start' point of the center of the screen, and an 'end' point centered on your mouse.
* Mousescroll up - Increases the brush size for block coloring
* Mousescroll down - Decreases the brush size for block coloring
* Shift + Mousescroll up - Increase block weight
* Shift + Mousescroll down - Decrease block weight
* Escape - Clears the blocks colored by the algorithm running. Does not remove 'Weighted blocks', 'Barriers', 'Start', or 'End'

## Settings menu
* Modify Algorithm dialog:
  * Setting which algorithm you want to run
  * Setting a weight coefficient that modifies how strongly weights are calculated
  * Setting hierarchical pathfinding

* Window menu
  * Allows you to set the number of blocks in each row and column, its size, and the size of 'Regions'

* Sleep Timer
  * Allows you to slow down the algorithm execution

## Weighting
Common to most pathfinding algorithms is the concept of weighted values. You can modify the weight of the blocks you color next using the scroll bar located at the bottom of the screen, above "Start Simulation"
* Weights of 0 are equivalent to a 'free' block, or a block that provides no restriction
* Weights between (0, 1) are considered partial weights and affect the algorithms path
* Weights of 1 are equivalent to 'barriers' that are not considered when the pathfinding algorithms run

## Oooh, Pretty colors

Weighting is represented on the screen by block color. Values of zero produce a very light grey color, and values of (0, 1) become increasingly dark. Values of 1.0 produce a extremely dark grey that is noticeably different than a value of 0.99

### A graph with only 'Barriers'
![](http://imgur.com/cRUfzab.png)

When you run the program, you will notice a few more colors jump out at you.
* Blue blocks are blocks located along the most optimal path found
* Off color grey blocks are blocks added to the "Open" queue, but not processed
* Colors of red to green are blocks that were considered by the algorithm. You will notice that colors closer to the 'end' are more green, and colors closer to the 'start' are more red.

## Hierarchical Pathfinding
Hierarchical pathfinding is an interesting way of calculating paths. I have implemented it by developing a system of 'Regions' where regions are considered connected if they share a common edge. Regions are bounded to a specific region of the map determined by a size variable in the Window menu. Multiple regions may coexist in the same area, but they must be disconnected by a 'Barrier', or a block with a weight of 1.0.

### An example of how regions work. This one has been split in half by a barrier
![](http://imgur.com/UUPB7KT.png)

Using the concept of regions, we can first pathfind through a region and then, based on the regions from beginning to end, limit the actual algorithms to run only in the space determined by the hierarchical approach.

This comes in handy when we consider Dijkstra's algorithm, because instead of pathfinding using a breadth first search approach, we modify it and only allow it to search in the region that the hierarchical pathfinding found.

This system is not necessary optimal, but I think it was an interesting experiment to include. This feature can be disabled through the Algorithm settings.

## Change detection
When running the program or adding 'Barriers' to the grid, you may notice that a square flashes a yellow color. This was included to represent region updates. A similar effect occurs when you run the hierarchical pathfinding but instead of flashing blue, it flashes yellow to show the regions that the pathfinding considers on the path from 'start' to 'end'

### Adding barriers rebuild regions
![](http://imgur.com/4CNYVyz.png)

## Running the program
Running the program is as simple as choosing a 'start' and 'end' box and then pressing the "Start Simulation" button or pressing the spacebar. You can pause the simulation midway by again pressing the spacebar. You can also modify the speed at which the algorithm runs by setting the sleep timer in the settings menu.

At the top right of the program you will notice a set of numbers. These numbers are very important in determining the efficiency of the algorithm
* Nodes Open - Shows the number of nodes added to the "Open" queue
* Nodes Closed - Shows the number of nodes added to the "Closed" queue
* PathfindingAlgorithms.Path length - Shows the length of the most optimal path found.
* Run Time - Shows the running time of the algorithm
