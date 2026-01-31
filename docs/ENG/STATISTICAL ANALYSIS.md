# STATISTICAL ANALYSIS FOR PRIMEWALKER

## Suggested Metrics

### 1. Geometric Metrics
- **Fractal Dimension** (box-counting method)
- **Fill Factor** (ratio of occupied area to the circumscribed square)
- **Center of Mass** of the System
- **Moments of Inertia** about the Axes

### 2. Topological Metrics
- **Number of Connected Components**
- **Minimum Spanning Tree Length**
- **Clustering Coefficient** for Vertices
- **Graph Diameter** (maximum distance between vertices)

### 3. Numerical Metrics
- **Distribution of Segment Lengths** (histogram)
- **Autocorrelation** of the Sequence of Differences
- **Mean and Variance** of Segment Lengths
- **Ratio of green/black segments**

### 4. Temporal metrics (during animation)
- **Rate of expansion** of the pattern
- **Rate of appearance of new objects**
- **Periodicity** in pattern emergence

## Analysis methods

### Correlation analysis
- Correlation between the length of a segment and its color
- Correlation between the direction and subsequent segments
- Cross-correlation with known sequences (twin primes, etc.)

### Cluster analysis
- Identifying clusters by color
- Clustering of shapes by shape
- Hierarchical clustering of objects

### Fourier series
- Spectral analysis of a sequence of differences
- Identifying periodicities
- System frequency characteristics

## Practical implementation

### Data export
```java
// CSV format:
// x1,y1,x2,y2,color,length,direction,isYoungerTwin