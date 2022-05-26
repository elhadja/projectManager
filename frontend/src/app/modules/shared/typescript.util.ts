export class TypeScriptUtil {
  public static getDiff(a: number | string , b: number | string) {
    const _a = typeof a === 'number' ? a : Number.parseInt(a);
    const _b = typeof b === 'number' ? b : Number.parseInt(b);
    
    return _a - _b;
  }

  public static  getSum(a: number | string , b: number | string): number {
    const _a = typeof a === 'number' ? a : Number.parseInt(a);
    const _b = typeof b === 'number' ? b : Number.parseInt(b);

    return _a + _b;
  }

  public static toString(num: number | undefined): string {
    return num != null ? `${num}` : '';
  }
}
