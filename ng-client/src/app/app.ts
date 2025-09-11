import { Component, signal, OnInit } from "@angular/core";
import { RouterOutlet, RouterLink } from "@angular/router";

@Component({
  selector: "app-root",
  imports: [RouterOutlet, RouterLink],
  templateUrl: "./app.html",
  styleUrl: "./app.css",
})
export class App implements OnInit {
  ngOnInit(): void {
    console.log("App initialized");
  }
  protected readonly title = signal("ng-client");
}
