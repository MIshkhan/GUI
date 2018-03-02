#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h> // added
#include <stdio.h> // added
#include "flute.h"

// added
double time_diff(struct timeval x , struct timeval y) { 
    double x_ms , y_ms;
     
    x_ms = (double)x.tv_sec*1000000 + (double)x.tv_usec;
    y_ms = (double)y.tv_sec*1000000 + (double)y.tv_usec;
    
    return y_ms - x_ms;
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

    /*added*/
    FILE *f = fopen("../durations.txt", "a");
    fprintf(f, "%d %.3f\n", d, time_diff(startTime , endTime)/1000000); //added
    fclose(f);
    
    printtree(flutetree);
    plottree(flutetree);
}

