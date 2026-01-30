import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SectorTreeSelect } from './sector-tree-select';

describe('SectorTreeSelect', () => {
  let component: SectorTreeSelect;
  let fixture: ComponentFixture<SectorTreeSelect>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SectorTreeSelect]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SectorTreeSelect);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
