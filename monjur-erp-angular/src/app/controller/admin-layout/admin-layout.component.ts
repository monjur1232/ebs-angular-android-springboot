import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent {
  activeMenu: string = '';

  toggleSubMenu(menu: string) {
    this.activeMenu = this.activeMenu === menu ? '' : menu;
  }
}
