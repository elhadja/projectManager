import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'textLength'
})
export class TextLengthPipe implements PipeTransform {

  transform(value: string, size: number): string {
    return size <= value.length ? value.substring(0, size - 1) + ' ...' : value;
  }

}
