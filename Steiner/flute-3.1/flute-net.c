#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h> // added
#include "flute.h"

// added
double time_diff(struct timeval x , struct timeval y) { 
    double x_ms , y_ms , diff;
     
    x_ms = (double)x.tv_sec*1000000 + (double)x.tv_usec;
    y_ms = (double)y.tv_sec*1000000 + (double)y.tv_usec;
     
    diff = (double)y_ms - (double)x_ms;
     
    return diff;
}

int main()
{
    int d=0;
    int x[MAXD], y[MAXD];
    Tree flutetree;
    int flutewl;
    struct timeval startTime , endTime; // added

    gettimeofday(&startTime , NULL); // added
    
    while (!feof(stdin)) {
        scanf("%d %d\n", &x[d], &y[d]);
        d++;
    }
    readLUT();
    flutetree = flute(d, x, y, ACCURACY);
    // printf("FLUTE wirelength = %d\n", flutetree.length);
    flutewl = flute_wl(d, x, y, ACCURACY);
    // printf("FLUTE wirelength (without RSMT construction) = %d\n", flutewl);

    gettimeofday(&endTime , NULL); //added
    double diff = time_diff(startTime , endTime);
    printf("%.0f", diff); //added
    
    printtree(flutetree);
    plottree(flutetree);
}

