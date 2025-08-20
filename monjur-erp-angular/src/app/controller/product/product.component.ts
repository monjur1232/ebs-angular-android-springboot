import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from 'src/app/model/product.model';
import { ProductCategory } from 'src/app/model/product-category.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  url = 'http://localhost:8080/product';
  products: Product[] = [];
  product: Product = new Product();
  updateProduct: Product = new Product();
  divStatus = false;

  catUrl = 'http://localhost:8080/product-category';
  productCategories: ProductCategory[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllProductCategories();
    this.product.status = 'Active';
  }

  save(prod: Product) {
    this.http.post(this.url, prod).toPromise().then(() => {
      this.getAll();
      this.product = new Product();
      this.product.status = 'Active';

      Swal.fire('Saved!', 'Product has been added.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to add product.', 'error');
    });
  }

  getAll() {
    this.http.get<Product[]>(this.url).subscribe(
      (response) => this.products = response
    );
  }

  edit(prod: Product) {
    this.updateProduct = Object.assign({}, prod);
    this.divStatus = true;
  }

  update(prod: Product) {
    this.http.put(this.url + '/' + prod.id, prod).toPromise().then(() => {
      this.getAll();
      this.updateProduct = new Product();
      this.divStatus = false;

      Swal.fire('Updated!', 'Product has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update product.', 'error');
    });
  }

  delete(prod: Product) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this product?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + prod.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Product has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete product.', 'error');
        });
      }
    });
  }

  getAllProductCategories() {
    this.http.get<ProductCategory[]>(this.catUrl).subscribe(
      (response) => {
        this.productCategories = response;
      }
    );
  }

  onProductCategorySelect(event: any) {
    const catCode = event.target.value;
    const selectedCat = this.productCategories.find(c => c.productCategoryCode == catCode);
    if (selectedCat) {
      this.product.productCategoryCode = selectedCat.productCategoryCode;
      this.product.productCategoryName = selectedCat.productCategoryName;
    }
  }

  onUpdateProductCategorySelect(event: any) {
    const catCode = event.target.value;
    const selectedCat = this.productCategories.find(c => c.productCategoryCode == catCode);
    if (selectedCat) {
      this.updateProduct.productCategoryCode = selectedCat.productCategoryCode;
      this.updateProduct.productCategoryName = selectedCat.productCategoryName;
    }
  }

}
