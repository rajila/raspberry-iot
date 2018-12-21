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
    public class FoodDispensersController : ApiController
    {
        private dispensatorServiceContext db = new dispensatorServiceContext();

        // GET: api/FoodDispensers
        public IQueryable<FoodDispenser> GetFoodDispenser()
        {
            return db.FoodDispenser;
        }

        // GET: api/FoodDispensers/5
        [ResponseType(typeof(FoodDispenser))]
        public async Task<IHttpActionResult> GetFoodDispenser(int id)
        {
            FoodDispenser foodDispenser = await db.FoodDispenser.FindAsync(id);
            if (foodDispenser == null)
            {
                return NotFound();
            }

            return Ok(foodDispenser);
        }

        // PUT: api/FoodDispensers/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutFoodDispenser(int id, FoodDispenser foodDispenser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != foodDispenser.FoodDispenserId)
            {
                return BadRequest();
            }

            db.Entry(foodDispenser).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FoodDispenserExists(id))
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

        // POST: api/FoodDispensers
        [ResponseType(typeof(FoodDispenser))]
        public async Task<IHttpActionResult> PostFoodDispenser(FoodDispenser foodDispenser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.FoodDispenser.Add(foodDispenser);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = foodDispenser.FoodDispenserId }, foodDispenser);
        }

        // DELETE: api/FoodDispensers/5
        [ResponseType(typeof(FoodDispenser))]
        public async Task<IHttpActionResult> DeleteFoodDispenser(int id)
        {
            FoodDispenser foodDispenser = await db.FoodDispenser.FindAsync(id);
            if (foodDispenser == null)
            {
                return NotFound();
            }

            db.FoodDispenser.Remove(foodDispenser);
            await db.SaveChangesAsync();

            return Ok(foodDispenser);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool FoodDispenserExists(int id)
        {
            return db.FoodDispenser.Count(e => e.FoodDispenserId == id) > 0;
        }
    }
}