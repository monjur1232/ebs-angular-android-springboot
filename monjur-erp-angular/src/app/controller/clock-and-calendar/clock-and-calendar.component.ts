import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-clock-calendar',
  templateUrl: './clock-and-calendar.component.html',
  styleUrls: ['./clock-and-calendar.component.css']
})
export class ClockAndCalendarComponent implements OnInit, OnDestroy {
  currentTime: Date = new Date();
  currentMonth: Date = new Date();
  selectedDate: Date = new Date();
  private timer: any;
  
  // Clock hands rotation
  hourRotation = 0;
  minuteRotation = 0;
  secondRotation = 0;
  
  // Analog clock markers
  markers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
  
  // Weekday names
  weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  
  // Calendar data
  daysInMonth: number[] = [];
  emptyDays: number[] = [];
  
  // Sample events
  events = [
    { date: new Date(), time: '09:00', title: 'Team Meeting', location: 'Conference Room A' },
    { date: new Date(), time: '11:30', title: 'Client Call', location: 'Online' },
    { date: new Date(), time: '14:00', title: 'Project Review', location: 'Meeting Room 3' },
    { date: new Date(new Date().setDate(new Date().getDate() + 1)), time: '10:00', title: 'Sprint Planning', location: 'Team Area' },
  ];
  
  get todaysEvents() {
    return this.events.filter(event => 
      event.date.getDate() === this.selectedDate.getDate() &&
      event.date.getMonth() === this.selectedDate.getMonth() &&
      event.date.getFullYear() === this.selectedDate.getFullYear()
    );
  }

  ngOnInit() {
    this.updateClock();
    this.timer = setInterval(() => this.updateClock(), 1000);
    this.generateCalendar();
  }

  ngOnDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  updateClock() {
    this.currentTime = new Date();
    
    // Calculate rotations for analog clock hands
    const hours = this.currentTime.getHours() % 12;
    const minutes = this.currentTime.getMinutes();
    const seconds = this.currentTime.getSeconds();
    
    this.hourRotation = (hours * 30) + (minutes * 0.5);
    this.minuteRotation = minutes * 6;
    this.secondRotation = seconds * 6;
  }

  generateCalendar() {
    const year = this.currentMonth.getFullYear();
    const month = this.currentMonth.getMonth();
    
    // Get first day of month and total days in month
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    
    // Calculate empty days at start
    this.emptyDays = Array(firstDay).fill(0);
    
    // Generate array of days
    this.daysInMonth = Array.from({length: daysInMonth}, (_, i) => i + 1);
  }

  previousMonth() {
    this.currentMonth = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth() - 1,
      1
    );
    this.generateCalendar();
  }

  nextMonth() {
    this.currentMonth = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth() + 1,
      1
    );
    this.generateCalendar();
  }

  isToday(day: number): boolean {
    const today = new Date();
    return (
      day === today.getDate() &&
      this.currentMonth.getMonth() === today.getMonth() &&
      this.currentMonth.getFullYear() === today.getFullYear()
    );
  }

  isSelected(day: number): boolean {
    return (
      day === this.selectedDate.getDate() &&
      this.currentMonth.getMonth() === this.selectedDate.getMonth() &&
      this.currentMonth.getFullYear() === this.selectedDate.getFullYear()
    );
  }

  selectDay(day: number) {
    this.selectedDate = new Date(
      this.currentMonth.getFullYear(),
      this.currentMonth.getMonth(),
      day
    );
  }
}
