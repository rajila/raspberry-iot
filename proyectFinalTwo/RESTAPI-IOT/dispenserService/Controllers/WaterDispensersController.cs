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
using dispenserService.Models;

namespace dispenserService.Controllers
{
    public class WaterDispensersController : ApiController
    {
        private dispenserServiceContext db = new dispenserServiceContext();

        // GET: api/WaterDispensers
        public IQueryable<WaterDispenser> GetWaterDispensers()
        {
            return db.WaterDispensers;
        }

        // GET: api/WaterDispensers/5
        [ResponseType(typeof(WaterDispenser))]
        public async Task<IHttpActionResult> GetWaterDispenser(int id)
        {
            WaterDispenser waterDispenser = await db.WaterDispensers.FindAsync(id);
            if (waterDispenser == null)
            {
                return NotFound();
            }

            return Ok(waterDispenser);
        }

        // PUT: api/WaterDispensers/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutWaterDispenser(int id, WaterDispenser waterDispenser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != waterDispenser.WaterDispenserId)
            {
                return BadRequest();
            }

            db.Entry(waterDispenser).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!WaterDispenserExists(id))
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

        // POST: api/WaterDispensers
        [ResponseType(typeof(WaterDispenser))]
        public async Task<IHttpActionResult> PostWaterDispenser(WaterDispenser waterDispenser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.WaterDispensers.Add(waterDispenser);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = waterDispenser.WaterDispenserId }, waterDispenser);
        }

        // DELETE: api/WaterDispensers/5
        [ResponseType(typeof(WaterDispenser))]
        public async Task<IHttpActionResult> DeleteWaterDispenser(int id)
        {
            WaterDispenser waterDispenser = await db.WaterDispensers.FindAsync(id);
            if (waterDispenser == null)
            {
                return NotFound();
            }

            db.WaterDispensers.Remove(waterDispenser);
            await db.SaveChangesAsync();

            return Ok(waterDispenser);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool WaterDispenserExists(int id)
        {
            return db.WaterDispensers.Count(e => e.WaterDispenserId == id) > 0;
        }
    }
}