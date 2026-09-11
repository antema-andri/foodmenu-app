export class DateUtils {

  /**
   * Convertit une date ISO (YYYY-MM-DD) en DD-MM-YYYY
   * @param isoDate string - format "2025-12-02"
   * @returns string - format "02-12-2025"
   */
  static toDDMMYYYY(isoDate: string): string {
    if (!isoDate) return '';

    const parts = isoDate.split('-'); // ["2025", "12", "02"]
    if (parts.length !== 3) return isoDate;

    const [year, month, day] = parts;

    return `${day}-${month}-${year}`;
  }

  /**
   * Formate une date ISO en "DD-MM-YYYY HH:MM:SS"
   * @param isoDateString - Date au format ISO
   * @returns Date formatée "DD-MM-YYYY HH:MM:SS"
   */
  static formatCustomDate(isoDateString: string): string {
    const date = new Date(isoDateString);
    
    if (isNaN(date.getTime())) {
      throw new Error(`Date invalide: ${isoDateString}`);
    }
    
    // Les composants sont corrects comme montré dans votre debug
    const day = String(date.getDate()).padStart(2, '0');        // "08"
    const month = String(date.getMonth() + 1).padStart(2, '0'); // "12"
    const year = date.getFullYear();                           // 2025
    
    const hours = String(date.getHours()).padStart(2, '0');    // "20"
    const minutes = String(date.getMinutes()).padStart(2, '0'); // "10"
    const seconds = String(date.getSeconds()).padStart(2, '0'); // "28"
    
    // FORMAT CORRIGÉ : "DD-MM-YYYY HH:MM:SS"
    // Vérifiez bien que c'est EXACTEMENT cette ligne de retour
    return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
  }

}
