import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProductCategory } from 'src/app/model/product-category.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-product-category',
  templateUrl: './product-category.component.html',
  styleUrls: ['./product-category.component.css']
})
export class ProductCategoryComponent implements OnInit {

  url = 'http://localhost:8080/product-category';
  productCategories: ProductCategory[] = [];
  productCategory: ProductCategory = new ProductCategory();
  updateProductCategory: ProductCategory = new ProductCategory();
  divStatus = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
  }

  save(cat: ProductCategory) {
    this.http.post(this.url, cat).toPromise().then(() => {
      this.getAll();
      this.productCategory = new ProductCategory();

      Swal.fire('Saved!', 'Product category has been added.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to add product category.', 'error');
    });
  }

  getAll() {
    this.http.get<ProductCategory[]>(this.url).subscribe(
      (response) => this.productCategories = response
    );
  }

  edit(cat: ProductCategory) {
    this.updateProductCategory = Object.assign({}, cat);
    this.divStatus = true;
  }

  update(cat: ProductCategory) {
    this.http.put(this.url + '/' + cat.id, cat).toPromise().then(() => {
      this.getAll();
      this.updateProductCategory = new ProductCategory();
      this.divStatus = false;

      Swal.fire('Updated!', 'Product category has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update product category.', 'error');
    });
  }

  delete(cat: ProductCategory) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this product category?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + cat.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Product category has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete product category.', 'error');
        });
      }
    });
  }
}
