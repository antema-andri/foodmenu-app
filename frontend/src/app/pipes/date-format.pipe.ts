import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

  transform(value: string | undefined): string {
    if (!value) return '';

    const parts = value.split('-'); // ["2025", "12", "02"]
    if (parts.length !== 3) return value;

    const [year, month, day] = parts;

    return `${day}-${month}-${year}`;
  }
}
