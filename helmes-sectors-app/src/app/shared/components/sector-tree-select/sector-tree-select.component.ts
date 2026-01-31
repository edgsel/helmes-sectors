import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { SectorResponseDTO } from '../../models/api.models';

interface FlatSector {
  id: number;
  name: string;
  level: number;
  isLeaf: boolean;
}

@Component({
  selector: 'app-sector-tree-select',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SectorTreeSelectComponent),
      multi: true
    }
  ],
  templateUrl: './sector-tree-select.component.html',
  styleUrl: './sector-tree-select.component.scss'
})
export class SectorTreeSelectComponent implements ControlValueAccessor {
  @Input() set sectors(value: SectorResponseDTO[]) {
    this.flatSectors = this.flattenTree(value);
  }

  protected flatSectors: FlatSector[] = [];
  protected selectedIds: number[] = [];
  protected disabled = false;

  private onChange: (value: number[]) => void = () => {
  };

  private onTouched: () => void = () => {
  };

  writeValue(value: number[]): void {
    this.selectedIds = value || [];
  }

  registerOnChange(fn: (value: number[]) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  protected onSelectionChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    const selected = Array.from(select.selectedOptions)
      .map(opt => Number(opt.value))
      .filter(id => !isNaN(id));

    this.selectedIds = selected;
    this.onChange(selected);
    this.onTouched();
  }

  protected getIndent(level: number): string {
    return '\u00A0\u00A0\u00A0\u00A0'.repeat(level);
  }

  protected isSelected(id: number): boolean {
    return this.selectedIds.includes(id);
  }

  private flattenTree(sectors: SectorResponseDTO[], level = 0): FlatSector[] {
    const result: FlatSector[] = [];

    for (const sector of sectors) {
      const isLeaf = !sector.children || sector.children.length === 0;
      result.push({
        id: sector.id,
        name: sector.name,
        level,
        isLeaf
      });

      if (sector.children && sector.children.length > 0) {
        result.push(...this.flattenTree(sector.children, level + 1));
      }
    }

    return result;
  }
}
