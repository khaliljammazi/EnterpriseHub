import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  protected readonly modules = ['Products', 'Inventory', 'Suppliers', 'Purchases', 'POS', 'Sales'];
}
