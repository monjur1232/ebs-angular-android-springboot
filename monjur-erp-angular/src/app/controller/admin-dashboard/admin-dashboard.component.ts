// admin-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChartConfiguration, ChartOptions, ChartType } from 'chart.js';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  
  // Summary Card Data
  summary = [
    { title: 'Total Revenue', icon: 'fas fa-money-bill-wave', value: 2548000, color: '#4e73df' },
    { title: 'Active Projects', icon: 'fas fa-project-diagram', value: 15, color: '#1cc88a' },
    { title: 'Pending Tasks', icon: 'fas fa-tasks', value: 28, color: '#f6c23e' },
    { title: 'System Alerts', icon: 'fas fa-bell', value: 7, color: '#e74a3b' }
  ];

  // Business Modules
  businessModules = [
    { name: 'HR Management', icon: 'fas fa-users-cog', stats: '150 Employees', color: '#4e73df', link: '/employee' },
    { name: 'Finance', icon: 'fas fa-money-bill-wave', stats: '254 Transactions', color: '#1cc88a', link: '/finance' },
    { name: 'Inventory', icon: 'fas fa-warehouse', stats: '42 Products', color: '#f6c23e', link: '/inventory' },
    { name: 'Projects', icon: 'fas fa-project-diagram', stats: '15 Active', color: '#36b9cc', link: '/projects' },
    { name: 'CRM', icon: 'fas fa-handshake', stats: '89 Clients', color: '#e74a3b', link: '/crm' },
    { name: 'Analytics', icon: 'fas fa-chart-pie', stats: '12 Reports', color: '#cc44a8', link: '/analytics' }
  ];

  // Recent Activities
  recentActivities = [
    { type: 'primary', icon: 'fas fa-file-invoice-dollar', message: 'New invoice #INV-2025-0072 created', time: '15 minutes ago' },
    { type: 'success', icon: 'fas fa-project-diagram', message: 'Project "EBS Upgrade" completed successfully', time: '2 hours ago' },
    { type: 'warning', icon: 'fas fa-exclamation-triangle', message: 'Low inventory alert for Product #P-2042', time: '5 hours ago' },
    { type: 'info', icon: 'fas fa-user-plus', message: 'New employee "Fatima Akter" onboarded', time: '1 day ago' },
    { type: 'danger', icon: 'fas fa-server', message: 'Scheduled maintenance in 2 days', time: '2 days ago' }
  ];

  // System Stats
  systemStats = [
    { name: 'CPU Usage', usage: 65, total: '8 Cores', status: 'Normal' },
    { name: 'Memory', usage: 42, total: '16GB', status: 'Normal' },
    { name: 'Storage', usage: 78, total: '1TB', status: 'Warning' },
    { name: 'Database', usage: 35, total: '500GB', status: 'Normal' },
    { name: 'Network', usage: 22, total: '1Gbps', status: 'Normal' },
    { name: 'Backup', usage: 91, total: '2TB', status: 'Critical' }
  ];

  // Quick Actions
  quickActions = [
    { name: 'New Project', icon: 'fas fa-plus', route: 'projects/add', color: '#4e73df' },
    { name: 'Create Invoice', icon: 'fas fa-file-invoice', route: 'finance/invoice', color: '#1cc88a' },
    { name: 'Add Employee', icon: 'fas fa-user-plus', route: 'employee/add', color: '#f6c23e' },
    { name: 'Profit & Loss', icon: 'fas fa-balance-scale', route: 'profit-and-loss', color: '#36b9cc' },
    { name: 'Generate Report', icon: 'fas fa-file-export', route: 'reports', color: '#e74a3b' },
    { name: 'System Settings', icon: 'fas fa-cog', route: 'settings', color: '#cc44a8' }
  ];

  // Chart Type (dynamic)
  chartType: ChartType = 'bar';

  // Bar Chart Data
  barChartData: ChartConfiguration<ChartType>['data'] = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    datasets: [
      {
        label: 'Revenue (BDT)',
        data: [2100000, 2200000, 2300000, 2250000, 2400000, 2450000, 2350000, 2500000, 2450000, 2550000, 2600000, 2548000],
        backgroundColor: 'rgba(78, 115, 223, 0.7)',
        borderColor: 'rgba(78, 115, 223, 1)',
        borderWidth: 1
      },
      {
        label: 'Expenses (BDT)',
        data: [1500000, 1550000, 1600000, 1580000, 1650000, 1700000, 1680000, 1750000, 1720000, 1800000, 1850000, 1820000],
        backgroundColor: 'rgba(231, 74, 59, 0.7)',
        borderColor: 'rgba(231, 74, 59, 1)',
        borderWidth: 1
      }
    ]
  };

  barChartOptions: ChartOptions<ChartType> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          font: {
            size: 12
          }
        }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            return context.dataset.label + ': ৳' + (context.raw as number).toLocaleString();
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          callback: function(value) {
            return '৳' + value.toLocaleString();
          }
        }
      }
    }
  };

  // Pie Chart Data
  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: ['Sales', 'Services', 'Subscriptions', 'Other'],
    datasets: [
      {
        data: [45, 30, 15, 10],
        backgroundColor: ['#4e73df', '#1cc88a', '#f6c23e', '#e74a3b'],
        hoverBackgroundColor: ['#2e59d9', '#17a673', '#dda20a', '#be2617'],
        hoverBorderColor: "rgba(234, 236, 244, 1)",
      }
    ]
  };

  pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'right',
        labels: {
          font: {
            size: 12
          }
        }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            return context.label + ': ' + context.raw + '%';
          }
        }
      }
    }
  };

  years = [2023, 2024, 2025];
  selectedYear = new Date().getFullYear();

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.updateCurrentDate();
  }

  updateCurrentDate() {
    const now = new Date();
    const options: Intl.DateTimeFormatOptions = { 
      weekday: 'long', 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    };
    document.getElementById('current-date')!.textContent = now.toLocaleDateString('en-US', options);
  }

  updateBarChart() {
    const baseRevenueData = [2100000, 2200000, 2300000, 2250000, 2400000, 2450000, 2350000, 2500000, 2450000, 2550000, 2600000, 2548000];
    const baseExpenseData = [1500000, 1550000, 1600000, 1580000, 1650000, 1700000, 1680000, 1750000, 1720000, 1800000, 1850000, 1820000];
    const multiplier = this.selectedYear === 2025 ? 1 : this.selectedYear === 2024 ? 0.9 : 0.8;
    this.barChartData.datasets[0].data = baseRevenueData.map(amount => Math.round(amount * multiplier));
    this.barChartData.datasets[1].data = baseExpenseData.map(amount => Math.round(amount * multiplier * 0.7));
    this.barChartData = {...this.barChartData};
  }

  setChartType(type: ChartType) {
    this.chartType = type;
  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]);
  }

  getPieBackgroundColor(i: number): string {
    const color = this.pieChartData.datasets[0].backgroundColor;
    if (Array.isArray(color)) {
      return color[i];
    }
    return typeof color === 'string' ? color : '#ccc';
  }

}
