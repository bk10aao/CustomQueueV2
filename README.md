# CustomQueue

Implementation of a Java Queue using a singly LinkedList. An Array backed version can be found [here](https://github.com/bk10aao/CustomQueue).

# Time Complexity

| Method                          | V1 (Array-Backed) | V2 (Linked-List Based) | Winner |
|:--------------------------------|:-----------------:|:----------------------:|:------:|
| **`Constructor()`**             |      $O(1)$       |         $O(1)$         |  Tie   |
| **`Constructor(int capacity)`** |      $O(1)$       |         $O(1)$         |  Tie   |
| **`Constructor(Collection)`**   |      $O(N)$       |         $O(N)$         |  Tie   |
| **`add(E)`**                    |      $O(N)$       |         $O(1)$         |   V2   |
| **`offer(E)`**                  |      $O(N)$       |         $O(1)$         |   V2   |
| **`addAll(Collection)`**        |      $O(N)$       |         $O(N)$         |  Tie   |
| **`poll()`**                    |      $O(1)$       |         $O(1)$         |  Tie   |
| **`remove()`**                  |      $O(1)$       |         $O(1)$         |  Tie   |
| **`peek()`**                    |      $O(1)$       |         $O(1)$         |  Tie   |
| **`element()`**                 |      $O(1)$       |         $O(1)$         |  Tie   |
| **`size()`**                    |      $O(1)$       |         $O(1)$         |  Tie   |
| **`isEmpty()`**                 |      $O(1)$       |         $O(1)$         |  Tie   |
| **`clear()`**                   |      $O(N)$       |         $O(1)$         |   V2   |
| **`contains(Object)`**          |      $O(N)$       |         $O(N)$         |  Tie   |
| **`containsAll(Collection)`**   |    $O(N + M)$     |       $O(N + M)$       |  Tie   |
| **`remove(Object)`**            |      $O(N)$       |         $O(N)$         |  Tie   |
| **`removeAll(Collection)`**     |  $O(N \cdot M)$   |     $O(N \cdot M)$     |  Tie   |
| **`retainAll(Collection)`**     |  $O(N \cdot M)$   |     $O(N \cdot M)$     |  Tie   |
| **`iterator()`**                |      $O(1)$       |         $O(1)$         |  Tie   |
| **`toArray()`**                 |      $O(N)$       |         $O(N)$         |  Tie   |
| **`toArray(T[])`**              |      $O(N)$       |         $O(N)$         |  Tie   |
| **`toString()`**                |      $O(N)$       |         $O(N)$         |  Tie   |

# Space Complexity

| Operation                       | V1 (Array-Backed) | V2 (Linked-List Based) |  Winner  |
|:--------------------------------|:-----------------:|:----------------------:|:--------:|
| **`Constructor()`**             |      $O(1)$       |         $O(1)$         |   Tie    |
| **`Constructor(int capacity)`** |      $O(N)$       |         $O(1)$         |    V2    |
| **`Constructor(Collection)`**   |      $O(N)$       |         $O(N)$         |   Tie    |
| **`add(E)`**                    |      $O(N)$       |         $O(1)$         |    V2    |
| **`offer(E)`**                  |      $O(N)$       |         $O(1)$         |    V2    |
| **`addAll(Collection)`**        |      $O(N)$       |         $O(N)$         |   Tie    |
| **`poll()`**                    |      $O(1)$       |         $O(1)$         |   Tie    |
| **`remove()`**                  |      $O(1)$       |         $O(1)$         |   Tie    |
| **`peek()`**                    |      $O(1)$       |         $O(1)$         |   Tie    |
| **`element()`**                 |      $O(1)$       |         $O(1)$         |   Tie    |
| **`size()`**                    |      $O(1)$       |         $O(1)$         |   Tie    |
| **`isEmpty()`**                 |      $O(1)$       |         $O(1)$         |   Tie    |
| **`clear()`**                   |      $O(1)$       |         $O(1)$         |   Tie    |
| **`contains(Object)`**          |      $O(1)$       |         $O(1)$         |   Tie    |
| **`containsAll(Collection)`**   |      $O(M)$       |         $O(M)$         |   Tie    |
| **`remove(Object)`**            |      $O(1)$       |         $O(1)$         |   Tie    |
| **`removeAll(Collection)`**     |      $O(M)$       |         $O(M)$         |   Tie    |
| **`retainAll(Collection)`**     |      $O(M)$       |         $O(M)$         |   Tie    |
| **`iterator()`**                |      $O(1)$       |         $O(1)$         |   Tie    |
| **`toArray()`**                 |      $O(N)$       |         $O(N)$         |   Tie    |
| **`toArray(T[])`**              |      $O(N)$       |         $O(N)$         |   Tie    |
| **`toString()`**                |      $O(N)$       |         $O(N)$         |   Tie    |

**Notes**:
- **n**: Total number of elements currently in the queue.
- **m**: Number of elements in the input collection.

# Performance Comparison

Geometric mean (ns/op) across all tested collection sizes (10,000–100,000 elements). Margins under 1.15x or less than 10 ns are treated as 
noise-level ties since the source data has no per-run error/variance to test true statistical significance.

| Method                    | V1 (Array-Backed) | V2 (Linked-List Based) | Margin  |            Winner            |
|:--------------------------|:------------------|:-----------------------|:-------:|:----------------------------:|
| `Constructor()`           | 25.3              | 22.6                   |  1.12x  | **Statistically Equivalent** |
| `Constructor(int)`        | 2,911.8           | 22.4                   | 129.84x |            **V2**            |
| `Constructor(Collection)` | 91,264.5          | 120,007.7              |  1.31x  |            **V1**            |
| `add(E)`                  | 119,216.1         | 103,781.0              |  1.15x  | **Statistically Equivalent** |
| `addAll(Collection)`      | 112,486.3         | 106,982.2              |  1.05x  | **Statistically Equivalent** |
| `clear()`                 | 112,681.1         | 107,521.4              |  1.05x  | **Statistically Equivalent** |
| `contains(Object)`        | 12,445.6          | 47,646.8               |  3.83x  |            **V1**            |
| `containsAll(Collection)` | 49,709.5          | 429,935.7              |  8.65x  |            **V1**            |
| `element()`               | 17.3              | 22.0                   |  1.27x  | **Statistically Equivalent** |
| `isEmpty()`               | 17.2              | 21.8                   |  1.26x  | **Statistically Equivalent** |
| `iterator()`              | 21,843.7          | 69,204.5               |  3.17x  |            **V1**            |
| `offer(E)`                | 120,332.5         | 110,614.0              |  1.09x  | **Statistically Equivalent** |
| `peek()`                  | 17.0              | 23.6                   |  1.39x  | **Statistically Equivalent** |
| `poll()`                  | 83,729.7          | 112,885.4              |  1.35x  |            **V1**            |
| `remove()`                | 83,895.4          | 111,759.0              |  1.33x  |            **V1**            |
| `remove(Object)`          | 108,519.6         | 157,508.4              |  1.45x  |            **V1**            |
| `removeAll(Collection)`   | 372,382.7         | 356,837.4              |  1.04x  | **Statistically Equivalent** |
| `retainAll(Collection)`   | 346,154.9         | 376,255.9              |  1.09x  | **Statistically Equivalent** |
| `size()`                  | 16.7              | 22.1                   |  1.32x  | **Statistically Equivalent** |
| `toArray()`               | 5,356.5           | 58,457.8               | 10.91x  |            **V1**            |
| `toArray(T[])`            | 28,549.3          | 66,628.8               |  2.33x  |            **V1**            |
| `toString()`              | 780,299.2         | 747,334.8              |  1.04x  | **Statistically Equivalent** |

# Performance Charts

#### Note: The following performance charts are designed to be viewed in dark mode.
![heatmap.png](PerfromanceCharts/heatmap.png)
![plot_Constructor__.png](PerfromanceCharts/plot_Constructor__.png)
![plot_Constructor_int_.png](PerfromanceCharts/plot_Constructor_int_.png)
![plot_Constructor_Collection_.png](PerfromanceCharts/plot_Constructor_Collection_.png)
![plot_add_E_.png](PerfromanceCharts/plot_add_E_.png)
![plot_addAll_Collection_.png](PerfromanceCharts/plot_addAll_Collection_.png)
![plot_clear__.png](PerfromanceCharts/plot_clear__.png)
![plot_contains_Object_.png](PerfromanceCharts/plot_contains_Object_.png)
![plot_containsAll_Collection_.png](PerfromanceCharts/plot_containsAll_Collection_.png)
![plot_element__.png](PerfromanceCharts/plot_element__.png)
![plot_isEmpty__.png](PerfromanceCharts/plot_isEmpty__.png)
![plot_iterator__.png](PerfromanceCharts/plot_iterator__.png)
![plot_offer_E_.png](PerfromanceCharts/plot_offer_E_.png)
![plot_peek__.png](PerfromanceCharts/plot_peek__.png)
![plot_poll__.png](PerfromanceCharts/plot_poll__.png)
![plot_remove__.png](PerfromanceCharts/plot_remove__.png)
![plot_remove_Object_.png](PerfromanceCharts/plot_remove_Object_.png)
![plot_removeAll_Collection_.png](PerfromanceCharts/plot_removeAll_Collection_.png)
![plot_retainAll_Collection_.png](PerfromanceCharts/plot_retainAll_Collection_.png)
![plot_size__.png](PerfromanceCharts/plot_size__.png)
![plot_toArray__.png](PerfromanceCharts/plot_toArray__.png)
![plot_toArray_T___.png](PerfromanceCharts/plot_toArray_T___.png)
![plot_toString__.png](PerfromanceCharts/plot_toString__.png)