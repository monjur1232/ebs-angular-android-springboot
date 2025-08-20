import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Designation } from 'src/app/model/designation.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-designation',
  templateUrl: './designation.component.html',
  styleUrls: ['./designation.component.css']
})
export class DesignationComponent implements OnInit {

  url = 'http://localhost:8080/designation';
  designations: Designation[] = [];
  designation: Designation = new Designation();
  updateDesignation: Designation = new Designation();
  divStatus = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
  }

  save(desig: Designation) {
    this.http.post(this.url, desig).toPromise().then(() => {
      this.getAll();
      this.designation = new Designation();

      Swal.fire('Saved!', 'Designation has been added.', 'success');
      }).catch(() => {
        Swal.fire('Error!', 'Failed to add designation.', 'error');
      });
  }

  getAll() {
    this.http.get<Designation[]>(this.url).subscribe(
      (response) => this.designations = response
    );
  }

  edit(desig: Designation) {
    this.updateDesignation = Object.assign({}, desig);
    this.divStatus = true;
  }

  update(desig: Designation) {
    this.http.put(this.url + '/' + desig.id, desig).toPromise().then(() => {
      this.getAll();
      this.updateDesignation = new Designation();
      this.divStatus = false;

      Swal.fire('Updated!', 'Designation has been updated.', 'success');
      }).catch(() => {
        Swal.fire('Error!', 'Failed to update designation.', 'error');
      });
  }

  delete(desig: Designation) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this designation?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
      }).then((result) => {
        if (result.isConfirmed) {
          this.http.delete(this.url + '/' + desig.id).toPromise().then(() => {
            this.getAll();
            Swal.fire('Deleted!', 'Designation has been deleted.', 'success');
            }).catch(() => {
              Swal.fire('Error!', 'Failed to delete designation.', 'error');
            });
        }
      });
  }

}
