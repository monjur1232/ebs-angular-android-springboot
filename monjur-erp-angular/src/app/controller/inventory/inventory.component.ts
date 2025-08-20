import { Component, OnInit } from '@angular/core';
import { Inventory } from 'src/app/model/inventory.model';
import { InventoryService } from 'src/app/service/inventory.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {
  inventories: Inventory[] = [];

  constructor(private inventoryService: InventoryService) { }

  ngOnInit(): void {
    this.getInventory();
  }

  getInventory(): void {
    this.inventoryService.getInventory().subscribe(
      (data) => {
        this.inventories = data;
      },
      (error) => {
        console.error('Error fetching inventory:', error);
      }
    );
  }
}
