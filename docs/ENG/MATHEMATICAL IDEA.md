# MATHEMATICAL BASIS OF PRIMEWALKER

## Construction Algorithm

### Input Data:
- `N` - Number of primes to analyze (starting with 7)
- `scale` - Display scale
- `imageSize` - Output image size

### Construction Steps:

1. **Sequence Generation**
```
P = [7, 11, 13, 17, 19, 23, 29, 31, 37, ...]
```

2. **Filtering of Elder Twins**
- For each prime p, check: is (p-2) prime and is it present in P?
- If yes, then p (the older twin) is excluded
- Remaining: 7, 11, 17, 23, 29, 37, ...

3. **Constructing a Polygonal Line**
```
Start: (0,0)
Direction: right (0°)

For i from 0 to len(P_filtered)-2:
gap = P_filtered[i+1] - P_filtered[i]
If P_filtered[i] is the younger twin:
color = green
Otherwise:
color = black

Draw a segment of length gap in the current direction
Rotate the direction by -90°
```

4. **Visual Markers**
- Red dot: start (0,0)
- Blue dot: end of the last segment
- Green lines: segments starting from the younger twins

## Mathematical Features

### Property 1: Orthogonality
All segments are strictly horizontal or vertical, creating a "square" geometry.

### Property 2: Filtration Symmetry
Removing older twins creates non-random gaps in the sequence.

### Property 3: Color Coding
Green lines correspond to younger twins, allowing their distribution to be studied visually.

## Theoretical Significance

The method transforms:
- **Numerical Properties** → **Geometric Shapes**
- **Statistical Regularities** → **Spatial Structures**
- **Distribution Hypotheses** → **Visual Patterns**

This opens up new possibilities for the intuitive understanding of complex mathematical concepts.