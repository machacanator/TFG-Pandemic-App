import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import KeenSlider, { KeenSliderInstance } from "keen-slider"

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss', "../../../node_modules/keen-slider/keen-slider.min.css"]
})
export class Carousel {

  @ViewChild("sliderRef") sliderRef: ElementRef<HTMLElement>
  
  @Input() listadoImagenes: any = [];

  currentSlide: number = 1
  dotHelper: Array<Number> = []
  slider: KeenSliderInstance = null

  ngAfterViewInit() {
    if(this.listadoImagenes?.length > 1) {
      setTimeout(() => {
        this.slider = new KeenSlider(this.sliderRef.nativeElement, {
          initial: this.currentSlide,
          loop: true,
          slideChanged: (s) => {
            this.currentSlide = s.track.details.rel
          },
        },
        [
          (slider) => {
            let timeout: any;
            let mouseOver = false
            function clearNextTimeout() {
              clearTimeout(timeout)
            }
            function nextTimeout() {
              clearTimeout(timeout)
              if (mouseOver) return
              timeout = setTimeout(() => {
                slider.next()
              }, 2000)
            }
            slider.on("created", () => {
              slider.container.addEventListener("mouseover", () => {
                mouseOver = true
                clearNextTimeout()
              })
              slider.container.addEventListener("mouseout", () => {
                mouseOver = false
                nextTimeout()
              })
              nextTimeout()
            })
            slider.on("dragStarted", clearNextTimeout)
            slider.on("animationEnded", nextTimeout)
            slider.on("updated", nextTimeout)
          },
        ])
        this.dotHelper = [
          ...Array(this.slider.track.details.slides.length).keys(),
        ]
      })
    }
  }

  ngOnDestroy() {
    if (this.slider) this.slider.destroy()
  }

}
