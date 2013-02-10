<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.common.dao.*"%>
<%@ page import="com.digitald4.common.component.Column"%>

<article class="container_12">
	<section class="grid_4">
			<div class="block-border"><div class="block-content">
				<h1>Medium calendar</h1>
							
				<div class="box"><p class="mini-infos">
					<strong>Medium calendar</strong>
				</p></div>
				
				<div class="medium-calendar">
					<div class="calendar-controls">
						<a href="#" class="calendar-prev" title="Previous month"><img src="images/cal-arrow-left.png" width="16" height="16"></a>
						<a href="#" class="calendar-next" title="Next month"><img src="images/cal-arrow-right.png" width="16" height="16"></a>
						Apr 2010
					</div>
					
					<table cellspacing="0">
						<thead>
							<tr>
								<th scope="col" class="week-end">Sun</th>
								<th scope="col">Mon</th>
								<th scope="col">Tue</th>
								<th scope="col">Wed</th>
								<th scope="col">Thu</th>
								<th scope="col">Fri</th>
								<th scope="col" class="week-end">Sat</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="week-end other-month">28</td>
								<td class="other-month">29</td>
								<td class="other-month">30</td>
								<td class="other-month">31</td>
								<td><a href="#">1</a></td>
								<td><a href="#"><span class="blue-corner">2</span></a></td>
								<td class="week-end"><a href="#">3</a></td>
							</tr>
							<tr>
								<td class="week-end"><a href="#">4</a></td>
								<td><a href="#">5<span class="nb-events">54</span></a></td>
								<td><a href="#">6</a></td>
								<td><a href="#">7<span class="nb-events">1</span></a></td>
								<td><a href="#"><span class="red-corner"><span class="blue-corner">8</span></span></a></td>
								<td><a href="#"><span class="today">9</span></a></td>
								<td class="week-end"><a href="#">10</a></td>
							</tr>
							<tr>
								<td class="week-end"><a href="#">11</a></td>
								<td><a href="#">12</a></td>
								<td><a href="#">13</a></td>
								<td><a href="#">14<span class="nb-events">1</span></a></td>
								<td><a href="#"><span class="red-corner">15</span><span class="nb-events">1</span></a></td>
								<td><a href="#">16</a></td>
								<td class="week-end"><a href="#">17</a></td>
							</tr>
							<tr>
								<td class="week-end"><a href="#">18</a></td>
								<td><a href="#">19</a></td>
								<td><a href="#">20<span class="nb-events">1</span></a></td>
								<td><a href="#">21</a></td>
								<td><a href="#">22<span class="nb-events">1</span></a></td>
								<td><a href="#"><span class="blue-corner">23</span></a></td>
								<td class="week-end"><a href="#"><span class="blue-corner">24</span></a></td>
							</tr>
							<tr>
								<td class="week-end"><a href="#">25</a></td>
								<td class="unavailable">26</td>
								<td class="unavailable">27</td>
								<td class="unavailable">28</td>
								<td><a href="#">29</a></td>
								<td><a href="#">30</a></td>
								<td class="week-end other-month"><div><span class="blue-corner">1</span><span class="nb-events">2</span></div></td><!-- div required to position elements -->
							</tr>
						</tbody>
					</table>
				</div>
				
			</div></div>
		</section>
		
		<section class="grid_8">
			<div class="block-border"><div class="block-content">
				<h1>Large calendar</h1>
				
				<div class="block-controls">
					<ul class="controls-buttons">
						<li><a href="#" title="Previous month"><img src="images/icons/fugue/navigation-180.png" width="16" height="16"></a></li>
						<li class="sep"></li>
						<li class="controls-block"><strong>June 2010</strong></li>
						<li class="sep"></li>
						<li><a href="#" title="Next month"><img src="images/icons/fugue/navigation.png" width="16" height="16"></a></li>
					</ul>
				</div>
				
				<div class="no-margin">
					<table cellspacing="0" class="calendar">
						<thead>
							<tr>
								<th scope="col" class="black-cell"><span class="success"></span></th>
								<th scope="col" class="week-end">Sunday</th>
								<th scope="col">Monday</th>
								<th scope="col">Tuesday</th>
								<th scope="col">Wednesday</th>
								<th scope="col">Thursday</th>
								<th scope="col">Friday</th>
								<th scope="col" class="week-end">Saturday</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th scope="row">13</th>
								<td class="week-end other-month">
									<span class="day">28</span>
								</td>
								<td class="other-month">
									<span class="day">29</span>
								</td>
								<td class="other-month">
									<span class="day">30</span>
								</td>
								<td class="other-month">
									<span class="day">31</span>
								</td>
								<td>
									<a href="#" class="day">1</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">2</a>
									<ul class="dot-events with-children-tip">
										<li><a href="#">Lena's birthday</a></li>
										<li><a href="#">Replace server hard drive</a></li>
										<li><a href="#">Max's birthday</a></li>
									</ul>
									<a href="#" class="add-event">Add</a>
								</td>
								<td class="week-end">
									<a href="#" class="day">3</a>
									<a href="#" class="add-event">Add</a>
								</td>
							</tr>
							<tr>
								<th scope="row">14</th>
								<td class="week-end">
									<a href="#" class="day">4</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">5</a>
									<a href="#" class="add-event">Add</a>
									<ul class="events">
										<li><a href="#"><b>9:00</b> Meeting</a></li>
										<li><a href="#"><b>11:00</b> Meeting with D.H.</a></li>
									</ul>
									<div class="more-events">
										3 more events
										<ul>
											<li><a href="#"><b>14:00</b> Meeting</a></li>
											<li><a href="#"><b>17:00</b> Soccer</a></li>
											<li><a href="#"><b>21:00</b> Diner with Jane</a></li>
										</ul>
									</div>
								</td>
								<td>
									<a href="#" class="day">6</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">7</a>
									<a href="#" class="add-event">Add</a>
									<ul class="events">
										<li><a href="#"><b>9:00</b> Meeting</a></li>
									</ul>
								</td>
								<td>
									<a href="#" class="day">8</a>
									<ul class="dot-events with-children-tip">
										<li class="red">Tax payment limit date</li>
										<li><a href="#">Check server hard drive logs</a></li>
									</ul>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day today">9</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td class="week-end">
									<a href="#" class="day">10</a>
									<a href="#" class="add-event">Add</a>
								</td>
							</tr>
							<tr>
								<th scope="row">15</th>
								<td class="week-end">
									<a href="#" class="day">11</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">12</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">13</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">14</a>
									<a href="#" class="add-event">Add</a>
									<ul class="events">
										<li class="red"><b>17:00</b> Meeting</li>
									</ul>
								</td>
								<td>
									<a href="#" class="day">15</a>
									<ul class="dot-events with-children-tip">
										<li class="red">John's birthday</li>
									</ul>
									<a href="#" class="add-event">Add</a>
									<ul class="events">
										<li><a href="#"><b>17:00</b> Soccer</a></li>
									</ul>
								</td>
								<td>
									<a href="#" class="day">16</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td class="week-end">
									<a href="#" class="day">17</a>
									<a href="#" class="add-event">Add</a>
								</td>
							</tr>
							<tr>
								<th scope="row">16</th>
								<td class="week-end">
									<a href="#" class="day">18</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">19</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">20</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">21</a>
									<a href="#" class="add-event">Add</a>
									<ul class="events">
										<li><a href="#"><b>8:00</b> Meeting</a></li>
									</ul>
								</td>
								<td>
									<a href="#" class="day">22</a>
									<a href="#" class="add-event">Add</a>
									<ul class="events">
										<li><a href="#"><b>17:00</b> Soccer</a></li>
									</ul>
								</td>
								<td>
									<a href="#" class="day">23</a>
									<ul class="dot-events with-children-tip">
										<li><a href="#">Check server hard drive logs</a></li>
									</ul>
									<a href="#" class="add-event">Add</a>
								</td>
								<td class="week-end">
									<a href="#" class="day">24</a>
									<ul class="dot-events with-children-tip">
										<li><a href="#">Send final report</a></li>
									</ul>
									<a href="#" class="add-event">Add</a>
								</td>
							</tr>
							<tr>
								<th scope="row">17</th>
								<td class="week-end">
									<a href="#" class="day">25</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td class="unavailable">
									<span class="day">26</span>
								</td>
								<td class="unavailable">
									<span class="day">27</span>
								</td>
								<td class="unavailable">
									<span class="day">28</span>
								</td>
								<td>
									<a href="#" class="day">29</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td>
									<a href="#" class="day">30</a>
									<a href="#" class="add-event">Add</a>
								</td>
								<td class="week-end other-month">
									<span class="day">1</span>
									<ul class="dot-events with-children-tip">
										<li>Tom's birthday</li>
									</ul>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<ul class="message no-margin">
					<li>18 events found</li>
				</ul>
			
			</div></div>
		</section>
</article>