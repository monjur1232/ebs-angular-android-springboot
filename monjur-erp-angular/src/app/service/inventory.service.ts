import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { Product } from '../model/product.model';
import { PurchaseOrder } from '../model/purchase-order.model';
import { Return } from '../model/return.model';
import { SalesOrder } from '../model/sales-order.model';
import { SalesReturn } from '../model/sales-return.model';
import { Inventory } from '../model/inventory.model';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private productUrl = 'http://localhost:8080/product';
  private poUrl = 'http://localhost:8080/purchase-order';
  private returnUrl = 'http://localhost:8080/return';
  private soUrl = 'http://localhost:8080/sales-order';
  private srUrl = 'http://localhost:8080/sales-return';

  constructor(private http: HttpClient) { }

  getInventory(): Observable<Inventory[]> {
    return forkJoin({
      products: this.http.get<Product[]>(this.productUrl),
      purchaseOrders: this.http.get<PurchaseOrder[]>(this.poUrl),
      returns: this.http.get<Return[]>(this.returnUrl),
      salesOrders: this.http.get<SalesOrder[]>(this.soUrl),
      salesReturns: this.http.get<SalesReturn[]>(this.srUrl)
    }).pipe(
      map(({products, purchaseOrders, returns, salesOrders, salesReturns}) => {
        return products.map(product => {
          const inventory = new Inventory();
          inventory.id = product.id;
          inventory.productCode = product.productCode;
          inventory.productName = product.productName;
          inventory.reorderLevel = product.reorderLevel ?? 0;

          // Calculate quantities
          inventory.purchasedQuantity = purchaseOrders
            .filter(po => po.productCode === product.productCode)
            .reduce((sum, po) => sum + (po.purchaseQuantity || 0), 0);

          inventory.returnedToSupplier = returns
            .filter(ret => ret.productCode === product.productCode)
            .reduce((sum, ret) => sum + (ret.returnQuantity || 0), 0);

          inventory.soldQuantity = salesOrders
            .filter(so => so.productCode === product.productCode)
            .reduce((sum, so) => sum + (so.salesQuantity || 0), 0);

          inventory.returnedFromCustomer = salesReturns
            .filter(sr => sr.productCode === product.productCode)
            .reduce((sum, sr) => sum + (sr.returnQuantity || 0), 0);

          // Calculate available quantity
          inventory.availableQuantity = 
            (inventory.purchasedQuantity - inventory.returnedToSupplier) - 
            (inventory.soldQuantity - inventory.returnedFromCustomer);

          return inventory;
        });
      })
    );
  }
}
