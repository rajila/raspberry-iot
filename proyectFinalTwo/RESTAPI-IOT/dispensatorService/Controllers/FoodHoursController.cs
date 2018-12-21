using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using dispensatorService.Models;

namespace dispensatorService.Controllers
{
    public class FoodHoursController : ApiController
    {
        private dispensatorServiceContext db = new dispensatorServiceContext();

        // GET: api/FoodHours
        public IQueryable<FoodHour> GetFoodHours()
        {
            return db.FoodHours;
        }

        // GET: api/FoodHours/5
        [ResponseType(typeof(FoodHour))]
        public async Task<IHttpActionResult> GetFoodHour(int id)
        {
            FoodHour foodHour = await db.FoodHours.FindAsync(id);
            if (foodHour == null)
            {
                return NotFound();
            }

            return Ok(foodHour);
        }

        // PUT: api/FoodHours/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutFoodHour(int id, FoodHour foodHour)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != foodHour.FoodHourId)
            {
                return BadRequest();
            }

            db.Entry(foodHour).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FoodHourExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/FoodHours
        [ResponseType(typeof(FoodHour))]
        public async Task<IHttpActionResult> PostFoodHour(FoodHour foodHour)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.FoodHours.Add(foodHour);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = foodHour.FoodHourId }, foodHour);
        }

        // DELETE: api/FoodHours/5
        [ResponseType(typeof(FoodHour))]
        public async Task<IHttpActionResult> DeleteFoodHour(int id)
        {
            FoodHour foodHour = await db.FoodHours.FindAsync(id);
            if (foodHour == null)
            {
                return NotFound();
            }

            db.FoodHours.Remove(foodHour);
            await db.SaveChangesAsync();

            return Ok(foodHour);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool FoodHourExists(int id)
        {
            return db.FoodHours.Count(e => e.FoodHourId == id) > 0;
        }
    }
}