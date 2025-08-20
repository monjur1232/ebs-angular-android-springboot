import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-profit-and-loss',
  templateUrl: './profit-and-loss.component.html',
  styleUrls: ['./profit-and-loss.component.css']
})
export class ProfitAndLossComponent implements OnInit {

  objectKeys = Object.keys;

  purchases: any[] = [];
  sales: any[] = [];

  filteredPurchases: any[] = [];
  filteredSales: any[] = [];

  purchaseSearchTerm = '';
  salesSearchTerm = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.http.get<any[]>('http://localhost:8080/purchase-order').subscribe(data => {
      this.purchases = data;
      this.filteredPurchases = data;
    });

    this.http.get<any[]>('http://localhost:8080/sales-order').subscribe(data => {
      this.sales = data;
      this.filteredSales = data;
    });
  }

  filterPurchases() {
    this.filteredPurchases = this.purchases.filter(p =>
      Object.values(p).some(val =>
        String(val).toLowerCase().includes(this.purchaseSearchTerm.toLowerCase())
      )
    );
  }

  filterSales() {
    this.filteredSales = this.sales.filter(s =>
      Object.values(s).some(val =>
        String(val).toLowerCase().includes(this.salesSearchTerm.toLowerCase())
      )
    );
  }

  get totalPurchase() {
    return this.filteredPurchases.reduce((sum, p) => sum + (p.totalAmount || 0), 0);
  }

  get totalSales() {
    return this.filteredSales.reduce((sum, s) => sum + (s.totalAmount || 0), 0);
  }

  get profitOrLoss() {
    return this.totalSales - this.totalPurchase;
  }

  get productSummary() {
    const summary: any = {};
    for (const p of this.filteredPurchases) {
      const key = p.productName;
      if (!summary[key]) summary[key] = { purchase: 0, sales: 0 };
      summary[key].purchase += p.totalAmount || 0;
    }
    for (const s of this.filteredSales) {
      const key = s.productName;
      if (!summary[key]) summary[key] = { purchase: 0, sales: 0 };
      summary[key].sales += s.totalAmount || 0;
    }
    return summary;
  }

  get supplierCount() {
    const unique = new Set(this.filteredPurchases.map(p => p.supplierCode));
    return unique.size;
  }

  get customerCount() {
    const unique = new Set(this.filteredSales.map(s => s.customerCode));
    return unique.size;
  }

  get supplierWisePurchase() {
    const summary: any = {};
    for (const p of this.filteredPurchases) {
      const key = p.supplierName || p.supplierCode;
      if (!summary[key]) summary[key] = 0;
      summary[key] += p.totalAmount || 0;
    }
    return summary;
  }

  get customerWiseSales() {
    const summary: any = {};
    for (const s of this.filteredSales) {
      const key = s.customerName || s.customerCode;
      if (!summary[key]) summary[key] = 0;
      summary[key] += s.totalAmount || 0;
    }
    return summary;
  }

}
