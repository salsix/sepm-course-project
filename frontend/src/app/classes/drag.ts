
export class Drag {
  grid = 5;
  canDrag = true;
  dragging = false;

  //mouse pos when starting drag
  posX: number;
  posY: number;

  //start pos of object starting drag
  startX: number;
  startY: number;

  public mousedown(event: MouseEvent){
    if(!this.canDrag || this.dragging){
      return;
    }
    this.posX = event.clientX;
    this.posY = event.clientY;
    this.dragging = true;
  }
}
