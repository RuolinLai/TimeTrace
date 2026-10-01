# TimeTrace

TimeTrace is a time-tracking application that records how users spend time on their tasks and turns that data into charts. It helps users understand where their time has gone, stay aware of distractions, and improve focus.

## Features

### Create Task Session
Users can create a new task session by entering:

- Task name
- Expected time to finish the task

### Task Timer
While a task is running, TimeTrace helps the user stay accountable.

#### Reminder
Every `n` minutes, the program checks in with the user to ask whether they are still working. The reminder interval `n` can be adjusted by the user.

If the user is not working, the program asks what they are doing instead.

#### Mark
The **Mark** button lets the user tell TimeTrace that they are going to do something else. TimeTrace records this as **not-working** time so distractions are captured accurately.

#### Done
The **Done** button ends the task and opens the **Task Done** scene. This scene shows the recorded data for the task, such as:

- Total time spent on the task
- Total distracted / not-working time
- Focus rate
- Time distribution for the specific task

### Task Records & Visualization
TimeTrace displays historical task records so users can review their habits over time.

Records can be shown as:

- Pie charts
- Grids / forms
- Bar charts

### AI Analysis
TimeTrace includes AI-powered analysis to help users understand their time usage.

It can:

- Identify which tasks received too much time
- Analyze why a task may have taken so long
- Evaluate whether the time spent was reasonable
- Give advice on how to improve time management and focus

## How It Works

1. Create a task and enter the expected duration.
2. Start the timer.
3. Receive periodic reminders every `n` minutes.
4. Use **Mark** when switching to something else.
5. Press **Done** when finished.
6. Review the task summary in the **Task Done** scene.
7. Explore charts and records across tasks.
8. Use AI analysis to find patterns and improve.

## Usage Example

1. Create a task called `Write project report`.
2. Set the expected time to `90 minutes`.
3. Start the task timer.
4. Every `15 minutes`, TimeTrace asks if you are still working.
5. If you switch to social media, click **Mark**.
6. When the report is finished, click **Done**.
7. View total time, distraction time, focus rate, and time distribution.
8. Check charts and AI analysis to understand your habits.

## Project Status

TimeTrace is currently a product concept / prototype. Implementation details, tech stack, and installation instructions will be added as the project develops.

## Roadmap

- [ ] Implement task creation
- [ ] Implement task timer and reminder system
- [ ] Implement **Mark** and **Done** actions
- [ ] Build **Task Done** scene
- [ ] Add pie charts, bar charts, and grid/form views
- [ ] Add AI analysis
- [ ] Add data persistence and export
- [ ] Add user settings for reminder intervals

## Contributing

Contributions, ideas, and feedback are welcome. Please open an issue or submit a pull request.

## License

License TBD.
