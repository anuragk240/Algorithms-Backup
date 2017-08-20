import java.util.ArrayList;

public class ErrorProneAssembly {
  public ErrorProneAssembly() {}
  
  public static void main(String[] paramArrayOfString) { java.util.Scanner localScanner = new java.util.Scanner(System.in);
    int i = 1618;
    int j = 100;
    StringBuilder[] arrayOfStringBuilder = new StringBuilder[i];
    for (int k = 0; k < i; k++) {
      arrayOfStringBuilder[k] = new StringBuilder(localScanner.nextLine());
    }
    
    Graph localGraph = buildGraph(arrayOfStringBuilder, i, j);
    
    i = localGraph.getVertices();
    
    int m = 0;
    ArrayList localArrayList1 = null;
    ArrayList localArrayList2 = null;
    
    for (int n = 0; n < i; n++) {
      boolean bool = localGraph.traverse(n, arrayOfStringBuilder);
      if ((bool) && (m < totalweight)) {
        m = totalweight;
        localArrayList1 = path;
        localArrayList2 = edges;
      }
    }
    

    boolean[] arrayOfBoolean = new boolean[localArrayList1.size()];
    java.util.Arrays.fill(arrayOfBoolean, false);
    arrayOfBoolean = errorCorrection(arrayOfStringBuilder, j, localArrayList1, localArrayList2, arrayOfBoolean);
    arrayOfBoolean = errorCorrection(arrayOfStringBuilder, j, localArrayList1, localArrayList2, arrayOfBoolean);
    
    String str = assembleGenome(arrayOfStringBuilder, localArrayList1, localArrayList2);
    
    System.out.println(str);
  }
  
  private static String assembleGenome(StringBuilder[] paramArrayOfStringBuilder, ArrayList<Integer> paramArrayList1, ArrayList<Integer> paramArrayList2) {
    StringBuilder localStringBuilder = new StringBuilder(paramArrayOfStringBuilder[((Integer)paramArrayList1.get(0)).intValue()]);
    

    for (int k = 1; k < paramArrayList1.size(); k++) {
      int j = ((Integer)paramArrayList1.get(k)).intValue();
      i = ((Integer)paramArrayList2.get(k - 1)).intValue();
      localStringBuilder.append(paramArrayOfStringBuilder[j].substring(i));
    }
    int i = ((Integer)paramArrayList2.get(paramArrayList1.size() - 1)).intValue();
    localStringBuilder.delete(0, i);
    return localStringBuilder.toString();
  }
  
  private static boolean[] errorCorrection(StringBuilder[] paramArrayOfStringBuilder, int paramInt, ArrayList<Integer> paramArrayList1, ArrayList<Integer> paramArrayList2, boolean[] paramArrayOfBoolean) {
    int i = paramArrayList1.size();
    
    for (int j = 0; j < i; j++) {
      int k = ((Integer)paramArrayList1.get(j)).intValue();
      int m = ((Integer)paramArrayList1.get((j + 1) % i)).intValue();
      int n = ((Integer)paramArrayList2.get(j)).intValue();
      int i1 = 0;
      int i2 = paramInt - n; for (int i3 = 0; (i2 < paramInt) && (i1 < 2); i3++) {
        if (paramArrayOfStringBuilder[k].charAt(i2) != paramArrayOfStringBuilder[m].charAt(i3)) {
          if (paramArrayOfBoolean[k] != 0) {
            paramArrayOfStringBuilder[m].setCharAt(i3, paramArrayOfStringBuilder[k].charAt(i2));
            paramArrayOfBoolean[m] = true;
            break; }
          if (paramArrayOfBoolean[m] != 0) {
            paramArrayOfStringBuilder[k].setCharAt(i2, paramArrayOfStringBuilder[m].charAt(i3));
            paramArrayOfBoolean[k] = true;
            break;
          }
          
          i1++;
          int i4 = ((Integer)paramArrayList1.get((j + i - 1) % i)).intValue();
          int i5 = ((Integer)paramArrayList2.get((j + i - 1) % i)).intValue();
          if (i5 > i2) {
            if (paramArrayOfStringBuilder[k].charAt(i2) == paramArrayOfStringBuilder[i4].charAt(paramInt - i5 + i2)) {
              paramArrayOfStringBuilder[m].setCharAt(i3, paramArrayOfStringBuilder[k].charAt(i2));
              paramArrayOfBoolean[m] = true;
              break label496; }
            if (paramArrayOfStringBuilder[m].charAt(i3) == paramArrayOfStringBuilder[i4].charAt(paramInt - i5 + i2)) {
              paramArrayOfStringBuilder[k].setCharAt(i2, paramArrayOfStringBuilder[m].charAt(i3));
              paramArrayOfBoolean[k] = true;
              
              break label496;
            }
          }
          int i6 = ((Integer)paramArrayList1.get((j + 1) % i)).intValue();
          int i7 = ((Integer)paramArrayList2.get((j + 1) % i)).intValue();
          int i8 = 2 * paramInt - i7 - n;
          if (i8 <= i2) {
            if (paramArrayOfStringBuilder[k].charAt(i2) == paramArrayOfStringBuilder[i6].charAt(i2 - i8)) {
              paramArrayOfStringBuilder[m].setCharAt(i3, paramArrayOfStringBuilder[k].charAt(i2));
              paramArrayOfBoolean[m] = true;
            }
            else if (paramArrayOfStringBuilder[m].charAt(i3) == paramArrayOfStringBuilder[i6].charAt(i2 - i8)) {
              paramArrayOfStringBuilder[k].setCharAt(i2, paramArrayOfStringBuilder[m].charAt(i3));
              paramArrayOfBoolean[k] = true;
            }
          }
        }
        label496:
        i2++;
      }
    }
    








































    return paramArrayOfBoolean;
  }
  
  private static Graph buildGraph(StringBuilder[] paramArrayOfStringBuilder, int paramInt1, int paramInt2)
  {
    int n;
    for (int i = 0; i < paramInt1; i++) {
      for (int j = i + 1; j < paramInt1; j++) {
        k = 1;
        m = 0;
        for (n = 0; n < paramInt2; n++) {
          if (paramArrayOfStringBuilder[i].charAt(n) != paramArrayOfStringBuilder[j].charAt(n)) { m++; if (m > 2)
              k = 0;
          }
        }
        if (k != 0) {
          paramArrayOfStringBuilder[i] = null;
          break;
        }
      }
    }
    
    for (i = 0; i < paramInt1; i++) {
      if (paramArrayOfStringBuilder[i] == null) {
        paramArrayOfStringBuilder[i] = paramArrayOfStringBuilder[(--paramInt1)];
        i--;
      }
    }
    
    ArrayList[] arrayOfArrayList1 = new ArrayList[paramInt1];
    ArrayList[] arrayOfArrayList2 = new ArrayList[paramInt1];
    for (int k = 0; k < paramInt1; k++) {
      arrayOfArrayList1[k] = new ArrayList();
      arrayOfArrayList2[k] = new ArrayList();
    }
    
    int[] arrayOfInt = new int[paramInt1];
    java.util.Arrays.fill(arrayOfInt, 0);
    
    for (int m = 0; m < paramInt1; m++) {
      for (n = 0; n < paramInt1; n++) {
        if (m != n)
        {
          for (int i1 = 1; (i1 <= paramInt2 - arrayOfInt[m]) && (i1 <= paramInt2 - 12); i1++) {
            int i2 = 1;
            int i3 = 0;
            for (int i4 = 0; i4 < paramInt2 - i1; i4++)
              if (paramArrayOfStringBuilder[m].charAt(i1 + i4) != paramArrayOfStringBuilder[n].charAt(i4)) { i3++; if (i3 > 2) {
                  i2 = 0;
                  break;
                }
              }
            if (i2 != 0) {
              arrayOfInt[m] = (paramInt2 - i1);
              arrayOfArrayList1[m].add(Integer.valueOf(n));
              arrayOfArrayList2[m].add(Integer.valueOf(arrayOfInt[m]));
              break;
            }
          }
        }
      }
    }
    Graph localGraph = new Graph(arrayOfArrayList1, arrayOfArrayList2);
    return localGraph;
  }
}
