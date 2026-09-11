export class PaginationUtils {
  static uiToApi(uiPage: number): number {
    return uiPage - 1;        // UI → API
  }

  static apiToUi(apiPage: number): number {
    return apiPage + 1;        // API → UI
  }

  static totalElements(totalPage: number, size: number): number {
    return totalPage * size; // substantially equal OR equal
  }
}
