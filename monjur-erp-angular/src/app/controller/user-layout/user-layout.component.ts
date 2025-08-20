import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-layout',
  templateUrl: './user-layout.component.html',
  styleUrls: ['./user-layout.component.css']
})
export class UserLayoutComponent {
  activeMenu: string = '';

  toggleSubMenu(menu: string) {
    this.activeMenu = this.activeMenu === menu ? '' : menu;
  }
}
