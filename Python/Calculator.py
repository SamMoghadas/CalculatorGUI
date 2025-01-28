import tkinter as tk

class Calculator:
    def __init__(self, root):
        self.root = root
        self.root.title("SAM Calculator")
        self.root.geometry("300x400")
        
        self.is_dark_mode = False
        self.display = tk.Entry(root, font=("Arial", 20), borderwidth=2, relief="ridge", justify="right")
        self.display.grid(row=0, column=0, columnspan=4, sticky="nsew")
        
        buttons = [
            '7', '8', '9', '/',
            '4', '5', '6', '*',
            '1', '2', '3', '-',
            'C', '0', '=', '+',
            '(', ')', '.', 'Back'
        ]
        
        for i, button in enumerate(buttons):
            tk.Button(root, text=button, width=5, height=2, command=lambda b=button: self.on_button_click(b)).grid(row=1+i//4, column=i%4, sticky="nsew")
        
        toggle_button = tk.Button(root, text="Dark/Light Mode", command=self.toggle_mode)
        toggle_button.grid(row=6, column=0, columnspan=4, sticky="nsew")

        self.root.bind('<Key>', self.on_key_press)
        
    def toggle_mode(self):
        self.is_dark_mode = not self.is_dark_mode
        bg_color = "black" if self.is_dark_mode else "white"
        fg_color = "white" if self.is_dark_mode else "black"
        
        self.display.config(bg=bg_color, fg=fg_color)
        for widget in self.root.winfo_children():
            if isinstance(widget, tk.Button):
                widget.config(bg=bg_color, fg=fg_color)
    
    def on_button_click(self, button):
        if button.isdigit() or button in "()":
            self.display.insert(tk.END, button)
        elif button == "C":
            self.display.delete(0, tk.END)
        elif button == "Back":
            self.display.delete(len(self.display.get())-1, tk.END)
        elif button == "=":
            try:
                result = eval(self.display.get())
                self.display.delete(0, tk.END)
                self.display.insert(tk.END, str(result))
            except Exception:
                self.display.delete(0, tk.END)
                self.display.insert(tk.END, "Error")
        else:
            self.display.insert(tk.END, " " + button + " ")

    def on_key_press(self, event):
        if event.keysym in '0123456789()':
            self.display.insert(tk.END, event.char)
        elif event.keysym == "Return":
            try:
                result = eval(self.display.get())
                self.display.delete(0, tk.END)
                self.display.insert(tk.END, str(result))
            except Exception:
                self.display.delete(0, tk.END)
                self.display.insert(tk.END, "Error")
        elif event.keysym in ("plus", "minus", "asterisk", "slash"):
            self.display.insert(tk.END, " " + event.char + " ")
        elif event.keysym == "BackSpace":
            self.display.delete(len(self.display.get())-1, tk.END)
        elif event.keysym == "C":
            self.display.delete(0, tk.END)

if __name__ == "__main__":
    root = tk.Tk()
    calc = Calculator(root)
    root.mainloop()
