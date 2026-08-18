import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {
  protected readonly links = [
    ['Dashboard', '/dashboard'], ['Products', '/products'], ['Inventory', '/inventory'],
    ['Suppliers', '/suppliers'], ['Purchases', '/purchases'], ['POS', '/pos'],
    ['Sales', '/sales'], ['Customers', '/customers'], ['Employees', '/employees'],
    ['Reports', '/reports'], ['Settings', '/settings'],
  ];
}
