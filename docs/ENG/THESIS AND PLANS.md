# THESES AND DEVELOPMENT PLANS

## Initial ideas

### Statistics for calculation:

1. Total number of primes
2. Total number of primes, excluding older twins, which are not counted when constructing the figure
3. Last prime
4. Last younger twin
5. Length of the last segment
6. Color of the last segment
7. Total length of the specified number of last segments
8. Total length of the specified number of last segments, excluding green
9. Total length of the specified number of last segments, excluding black
10. Sum of all primes
11. Sum of all primes, excluding twins
12. Sum of all prime twins
13. Sum of MB
14. Sum of SB
15. Number of pairs of twins
16. Difference between the sums of all MB and all SB
17. Total number of lines
18. Number of green lines
19. Number of black lines
20. Size of the circumscribed circle
21. Size of the circumscribed square
22. Size of the circumscribed n-gon - for a given number of angles
23. Lateral span - right and left. The entire drawing.
24. Height growth of the entire drawing
25. Downward growth of the entire drawing
26. Rightward growth of the entire drawing
27. Leftward growth of the entire drawing

___
# Line count - length
Counting lines of different colors together
Counting green lines separately
Counting black lines separately
1. All lines
2. Horizontal
3. Vertical
4. Longest green
5. Shortest green
6. Longest black
7. Shortest black
___
## Direction of drawn lines
Number of lines:
1. Right
2. Left
3. Down
4. Up
### Dimensioning:
- Portal lines as boundaries between dimensions
- Three principles of portal display
- Red and blue dots for marking the beginning and end of objects

### Interface improvements:
- "Recommended drawing size" button
- "Path to" button "drawing" for quick access
- Visualization of layers and overlays

## Observations and discoveries

### At N=2222, the following was discovered:
1. **Three objects**: parent and two children
2. **System orientation**: child objects are parallel to the parent
3. **Connection structure**: thread-like structure with two nodes
4. **Horizontal balance**: red and blue dots are almost in line
5. **Vertical asymmetry**: expansion predominantly downwards

### SCS (Object Connection Strength) property:
- Determined by the number of lines connecting structures
- Green connections vs. black connections
- Topological connectivity metric

## Development plans

### Short term (version 1.1):
- [ ] Implementation of all statistics (25+ parameters)
- [ ] Automatic image sizing
- [ ] Saving statistics in CSV
- [ ] Notification timer

### Medium Term (version 2.0):
- [ ] Visualization of dimensions and portals
- [ ] Construction animation
- [ ] Export in SVG format
- [ ] Obsidian plugin

### Long Term (version 3.0):
- [ ] 3D visualization
- [ ] Machine learning for pattern detection
- [ ] JavaScript web version
- [ ] Scientific publication

## Scientific hypotheses to test

1. **Fractality Hypothesis**: Structures are self-similar as N increases
2. **Twin Linkage Hypothesis**: Green lines form regular patterns
3. **Balance Hypothesis**: Red and blue dots tend to align horizontally
4. **Dimensionality Hypothesis**: The system has a non-integer fractal dimension